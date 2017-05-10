package com.khalsa.gurankassingh.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khalsa.gurankassingh.criminalintent.database.CrimeBaseHelper;
import com.khalsa.gurankassingh.criminalintent.database.CrimeCursorWrapper;
import com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE;
import static com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED;
import static com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SUSPECT;
import static com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE;
import static com.khalsa.gurankassingh.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID;

/**
 * Created by Gurankas Singh on 3/13/2017.
 */

public class CrimeLab
{
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context)
    {
       // mCrimes = new ArrayList<>();
       /* for (int i = 0; i< 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" +i);
            crime.setSolved(i%2 == 0);      //every other one
            mCrimes.add(crime);
        }*/
       mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Crime> getCrimes()
    {
        //return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return crimes;
    }

    public void addCrime(Crime c)
    {
       // mCrimes.add(c);
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }

    public Crime getCrime(UUID id)
    {
        /*for (Crime crime: mCrimes)
        {
            if(crime.getId().equals(id))
            {
                return crime;
            }
        }*/
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try
        {
            if (cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally
        {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,UUID+" = ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(UUID, crime.getId().toString());
        values.put(TITLE,crime.getTitle());
        values.put(DATE,crime.getDate().getTime());
        values.put(SOLVED,crime.isSolved()?1:0);
        values.put(SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }

    public static CrimeLab get(Context context)
    {
        if (sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
}
