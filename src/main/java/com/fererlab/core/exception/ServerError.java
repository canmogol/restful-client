package com.fererlab.core.exception;

import javax.ws.rs.WebApplicationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerError {

    private Exception rootException = null;
    private String[] exceptionClasses = new String[]{};
    private PossibleCause[] possibleCauses = new PossibleCause[]{};
    private String[] errorMessages = new String[]{};

    public ServerError(WebApplicationException webApplicationException) {
        this.rootException = webApplicationException;
        if (webApplicationException.getResponse().getHeaders().containsKey("ErrorMessage")) {
            this.errorMessages = webApplicationException.getResponse().getHeaderString("ErrorMessage").split("\\|");
        }
        if (webApplicationException.getResponse().getHeaders().containsKey("ExceptionClasses")) {
            this.exceptionClasses = webApplicationException.getResponse().getHeaderString("ExceptionClasses").split("\\|");
        }
        if (webApplicationException.getResponse().getHeaders().containsKey("PossibleCauses")) {
            this.possibleCauses = parseCauses(webApplicationException.getResponse().getHeaderString("PossibleCauses").split(";"));
        }
    }

    private PossibleCause[] parseCauses(String[] possibleCauses) {
        List<PossibleCause> possibleCauseList = new ArrayList<>();
        for (String possibleCauseParts : possibleCauses) {
            String[] causeParts = possibleCauseParts.split("\\|");
            try {
                possibleCauseList.add(new PossibleCause(
                        causeParts[0],
                        causeParts[1],
                        causeParts[2],
                        causeParts[3]
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return possibleCauseList.toArray(new PossibleCause[possibleCauseList.size()]);
    }

    public String[] getExceptionClasses() {
        return exceptionClasses;
    }

    public PossibleCause[] getPossibleCauses() {
        return possibleCauses;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }

    public Exception getRootException() {
        if (rootException instanceof WebApplicationException && exceptionClasses.length > 0) {
            try {
                String rootExceptionClassName = exceptionClasses[exceptionClasses.length - 1];
                Class<?> clazz = Class.forName(rootExceptionClassName);
                if (Exception.class.isAssignableFrom(clazz)) {
                    Constructor<?> constructor = null;
                    try {
                        constructor = clazz.getConstructor(String.class);
                        String messages = "";
                        for (String message : getErrorMessages()) {
                            messages += message + ", ";
                        }
                        rootException = (Exception) constructor.newInstance(messages);
                    } catch (NoSuchMethodException e) {
                        // ignore this exception, we will raise another if we cannot find the default constructor
                    }
                    if (constructor == null) {
                        constructor = clazz.getConstructor();
                        rootException = (Exception) constructor.newInstance();
                    }
                } else {
                    // this is not an Exception class, do nothing
                }
            } catch (NoSuchMethodException | InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                // we can safely ignore this exception, it means this type of exception is not available for client, root exception stays as WebApplicationException
                e.printStackTrace();
            }
        }
        return rootException;
    }

    @Override
    public String toString() {
        return "ERROR " + (Arrays.toString(exceptionClasses)) + " " + (Arrays.toString(errorMessages)) + Arrays.toString(possibleCauses);
    }

    public class PossibleCause {

        private final String className;
        private final String fileName;
        private final String methodName;
        private final String lineNumber;

        public PossibleCause(String className, String fileName, String methodName, String lineNumber) {

            this.className = className;
            this.fileName = fileName;
            this.methodName = methodName;
            this.lineNumber = lineNumber;
        }

        public String getClassName() {
            return className;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        @Override
        public String toString() {
            return "\n\t" + "at " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")";
        }
    }
}
