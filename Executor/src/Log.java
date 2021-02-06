public enum Log {
    OPENING_FILE("Could't open file: "),
    READING_FILE("Could't read file: "),
    NOT_ENOUGH_PARAMETERS("Not enough parameters in config file: "),
    CLOSING_BUFFER("Closing buffer error"),
    WRONG_NUM_OF_ELEMENTS("Wrong number of elements for "),
    INCORRECT_DATA_FORMAT("Incorrect data format for ");

    public final String message;

    Log(String message)
    {
        this.message = message;
    }
}
