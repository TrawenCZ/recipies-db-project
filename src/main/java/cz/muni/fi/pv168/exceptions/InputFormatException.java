package cz.muni.fi.pv168.exceptions;

public class InputFormatException extends Exception {

    public InputFormatException(){
        super();
    }

    public InputFormatException(String message){
        super(message);
    }

    public InputFormatException(String message, Throwable cause){
        super(message, cause);
    }

    // public static void qreaterEqualZero(String text, String label, boolean includingZero) throws InputFormatException {
    //     try {
    //         int number = Integer.parseInt(text);
    //         if (includingZero && number == 0) return;
    //         if (number <= 0) throw new NumberFormatException();
    //     } catch (NumberFormatException ex){
    //         String message;
    //         if (includingZero) {
    //             message = "zero or greater";
    //         } else {
    //             message = "greater than zero";
    //         }
    //         throw new InputFormatException("The text you have entered in the '"
    //                 + label + "' field is" +
    //                 " not a whole number or is not " + message + "."
    //         );
    //     }
    // }

    // public static void nonNegativeNumberText(String text, String label, boolean includingZero) throws InputFormatException {
    //     try {
    //         double number = Double.parseDouble(text);
    //         if (includingZero && number == 0) return;
    //         if (number <= 0) throw new NumberFormatException();
    //     } catch (NumberFormatException ex){
    //         String message;
    //         if (includingZero) {
    //             message = "zero or greater";
    //         } else {
    //             message = "greater than zero";
    //         }
    //         throw new InputFormatException("The text you have entered in the '"
    //                                         + label + "' field is" +
    //                                         " not a number or is not " + message + "."
    //         );
    //     }
    // }
}
