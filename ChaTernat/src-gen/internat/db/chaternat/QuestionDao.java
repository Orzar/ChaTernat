package internat.db.chaternat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import internat.db.chaternat.Question;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table QUESTION.
*/
public class QuestionDao extends AbstractDao<Question, Long> {

    public static final String TABLENAME = "QUESTION";

    /**
     * Properties of entity Question.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property QuestionNumber = new Property(1, Integer.class, "questionNumber", false, "QUESTION_NUMBER");
        public final static Property Text = new Property(2, String.class, "text", false, "TEXT");
        public final static Property LastAsked = new Property(3, java.util.Date.class, "lastAsked", false, "LAST_ASKED");
    };

    private DaoSession daoSession;


    public QuestionDao(DaoConfig config) {
        super(config);
    }
    
    public QuestionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'QUESTION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'QUESTION_NUMBER' INTEGER," + // 1: questionNumber
                "'TEXT' TEXT NOT NULL ," + // 2: text
                "'LAST_ASKED' INTEGER);"); // 3: lastAsked
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'QUESTION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Question entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer questionNumber = entity.getQuestionNumber();
        if (questionNumber != null) {
            stmt.bindLong(2, questionNumber);
        }
        stmt.bindString(3, entity.getText());
 
        java.util.Date lastAsked = entity.getLastAsked();
        if (lastAsked != null) {
            stmt.bindLong(4, lastAsked.getTime());
        }
    }

    @Override
    protected void attachEntity(Question entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Question readEntity(Cursor cursor, int offset) {
        Question entity = new Question( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // questionNumber
            cursor.getString(offset + 2), // text
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)) // lastAsked
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Question entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setQuestionNumber(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setText(cursor.getString(offset + 2));
        entity.setLastAsked(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Question entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Question entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
