package app.di_v.note.database;

/**
 * DataBase Schema
 * @author Dmitry Vaganov
 * @version 1.1.0
 */
public class NoteDb {
    public static final class NoteTable {
        public static final String NAME = "notes";

        /**
         * Definition of table columns
         */
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String COLOR = "color";
            public static final String DATE = "date";
            public static final String IMPORTANT = "important";
        }
    }
}
