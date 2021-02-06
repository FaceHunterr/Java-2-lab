public enum Log {
    LOG_FILE_NAME("Log"),
    FILE_HANDLER("Could't create FileHandler"),
    OPENING_FILE("Could't open file: "),
    READING_FILE("Could't read file: "),
    NOT_ENOUGH_PARAMETERS("Not enough parameters in config file: "),
    CLOSING_BUFFER("Closing buffer error"),
    WRONG_NUM_OF_ELEMENTS("Wrong number of elements for "),
    EXECUTORS_AND_CONFIGS("Еhe number of executors and configs does not match"),
    WRONG_CLASS_NAME("Wrong class name: "),
    PIPELINE("Error wit pipeline"),
    INVALID_ARGUMENT("Invalid command line argument");

    public final String message;

    Log(String message)
    {
        this.message = message;
    }
}
