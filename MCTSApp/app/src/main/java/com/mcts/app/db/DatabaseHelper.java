package com.mcts.app.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.mcts.app.R;
import com.mcts.app.model.AntenatalService;
import com.mcts.app.model.DeliveryDetails;
import com.mcts.app.model.Family;
import com.mcts.app.model.HighRiskSymtoms;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.PregnantWomen;
import com.mcts.app.model.Religion;
import com.mcts.app.model.WomenHighRisk;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {
    @SuppressLint("SdCardPath")

    private SQLiteDatabase mDataBase;
    private Context mContext;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DBConstant.DB_NAME, null, 1);
        this.mContext = context;
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    private boolean checkDataBase() {
        boolean strBool = false;
        try {
            String myPath = DBConstant.DB_PATH + DBConstant.DB_NAME;
            File dbFile = mContext.getDatabasePath(myPath);
            strBool = dbFile.exists();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return strBool;
    }

    private void copyDataBase() throws IOException {

        String outFileName = DBConstant.DB_PATH + DBConstant.DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = mContext.getAssets().open(DBConstant.DB_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    public void writeDatabase() throws SQLException {
        String myPath = DBConstant.DB_PATH + DBConstant.DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void readDatabase() throws SQLException {
        String myPath = DBConstant.DB_PATH + DBConstant.DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void closeDataBase() throws SQLException {
        mDataBase.close();
    }

    @Override
    public synchronized void close() {

        if (mDataBase != null)
            mDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//		db.execSQL(ins0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public ArrayList<Religion> getReligionData() {

        ArrayList<Religion> religionArrayList = new ArrayList<>();
        Religion rel = new Religion();
        rel.setId("000");
        rel.setName(mContext.getResources().getString(R.string.select_family_dharm));
        religionArrayList.add(rel);
        readDatabase();
        try {
            String selectQuery = "SELECT  * FROM tbl_religion";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Religion religion = new Religion();
                    religion.setId(cursor.getString(cursor.getColumnIndex("religionId")));
                    religion.setName(cursor.getString(cursor.getColumnIndex("religion")));
                    religionArrayList.add(religion);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return religionArrayList;
    }

    public ArrayList<Religion> getCastData() {

        ArrayList<Religion> religionArrayList = new ArrayList<>();
        Religion rel = new Religion();
        rel.setId("000");
        rel.setName(mContext.getResources().getString(R.string.select_family_cast));
        religionArrayList.add(rel);
        readDatabase();
        try {
            String selectQuery = "SELECT  * FROM tbl_racial";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Religion religion = new Religion();
                    religion.setId(cursor.getString(cursor.getColumnIndex("racialId")));
                    religion.setName(cursor.getString(cursor.getColumnIndex("racial")));
                    religionArrayList.add(religion);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return religionArrayList;
    }

    public ArrayList<MaritalStatus> getAganvadi(String villageId, String subCenterId) {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.select_aaganvadi));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  * FROM tbl_anganwadi WHERE villageId='" + villageId + "' and subcentreId='" + subCenterId + "'";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            Log.e("getAganvadi", selectQuery);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("anganwadiId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("anganwadi")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }


    public boolean updateFamilyDetails(Member member) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("houseNumber", member.getHouseNumber());
            contentValues.put("landmark", member.getLandmark());
            contentValues.put("landmark", member.getLandmark());
            contentValues.put("religionId", member.getReligionId());
            contentValues.put("racialId", member.getRaciald());
            contentValues.put("isBpl", member.getIsBpl());
            contentValues.put("bplNumber", member.getBplNumber());
            contentValues.put("anganwadiId", member.getAnganwadiId());
            contentValues.put("rationcardNrumber", member.getRationcardNrumber());
            contentValues.put("rsbycardNumber", member.getRsbycardNumber());
            contentValues.put("macardNumber", member.getMacardNumber());
            contentValues.put("updatedDate", dateFormat.format(date));
            if (member.getLattitudes() != null) {
                contentValues.put("lattitudes", member.getLattitudes());
                contentValues.put("longitude", member.getLongitude());
            }
            if (member.getFaliyu() != null) {
                contentValues.put("faliyaId", member.getFaliyu());
            }
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + member.getEmamtafamilyId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean createNewFamily(Member member) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();
        try {

            String sql = "Insert into " + DBConstant.FAMILY_TABLE + "(emamtafamilyId,villageId,subcentreId," +
                    "houseNumber,faliyaId,landmark,racialId,religionId,isBpl,bplNumber,rationcardNrumber,rsbycardNumber," +
                    "macardNumber,lattitudes,longitude,createdbyuserId,createdDate,isActive,anganwadiId,migrationtypeId,isUpdated" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, member.getEmamtafamilyId());
            insert.bindString(2, member.getVillageId());
            insert.bindString(3, member.getSubCenterId());
            if (member.getHouseNumber() != null) {
                insert.bindString(4, member.getHouseNumber());
            }
            insert.bindString(5, member.getFaliyu());
            if (member.getLandmark() != null) {
                insert.bindString(6, member.getLandmark());
            }
            insert.bindString(7, member.getRaciald());
            insert.bindString(8, member.getReligionId());
            insert.bindString(9, member.getIsBpl());
            if (member.getBplNumber() != null) {
                insert.bindString(10, member.getBplNumber());
            }
            if (member.getRationcardNrumber() != null) {
                insert.bindString(11, member.getRationcardNrumber());
            }
            if (member.getRsbycardNumber() != null) {
                insert.bindString(12, member.getRsbycardNumber());
            }
            if (member.getMacardNumber() != null) {
                insert.bindString(13, member.getMacardNumber());
            }

            if (member.getLattitudes() != null) {
                insert.bindString(14, member.getLattitudes());
                insert.bindString(15, member.getLongitude());
            }
            insert.bindString(16, member.getUserId());
            insert.bindString(17, dateFormat.format(date));
            insert.bindString(18, "1");
            if (member.getAnganwadiId() != null) {
                insert.bindString(19, member.getAnganwadiId());
            }
            insert.bindString(20, "1");
            insert.bindString(21, "1");
            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean createMember(Member member) {

        try {
            Member lastMember = getLastInsertedFamlily();
            mDataBase = getWritableDatabase();
            mDataBase.beginTransaction();

            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(familyId,emamtafamilyId,villageId,photo,photovalue," +
                    "firstName,middleName,lastName,isHead,gender,maritalStatus,birthDate,mobileNo,createdbyuserId," +
                    "createdDate,isActive,emamtahealthId,isUpdated" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, lastMember.getFamilyId());
            insert.bindString(2, lastMember.getEmamtafamilyId());
            insert.bindString(3, member.getVillageId());
            if (member.getPhoto() != null) {
                insert.bindString(4, member.getPhoto());
            }
            if (member.getPhotoValue() != null) {
                insert.bindString(5, member.getPhotoValue());
            }
            insert.bindString(6, member.getFirstName());
            insert.bindString(7, member.getMiddleName());
            insert.bindString(8, member.getLastName());
            insert.bindString(9, "1");
            insert.bindString(10, member.getGender());
            insert.bindString(11, member.getMaritalStatus());
            insert.bindString(12, member.getBirthDate());
            insert.bindString(13, member.getMobileNo());
            insert.bindString(14, member.getUserId());
            insert.bindString(15, dateFormat.format(date));
            insert.bindString(16, "1");
            insert.bindString(17, member.getEmamtahealthId());
            insert.bindString(18, "1");
            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public Member getLastInsertedFamlily() {

        Member member = new Member();
        mDataBase = getReadableDatabase();
        mDataBase.beginTransaction();
        try {
            String selectQuery = "SELECT familyId,emamtafamilyId " +
                    "FROM tbl_family " +
                    "ORDER BY familyId DESC LIMIT 1";
//                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.villageId='" + villageId + "'";
            Log.e("Member Search", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    member.setFamilyId(cursor.getString(cursor.getColumnIndex("familyId")));
                    member.setEmamtafamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return member;
    }

    public Member getLastInsertedMember() {

        Member member = new Member();
        mDataBase = getReadableDatabase();
        mDataBase.beginTransaction();
        try {
            String selectQuery = "SELECT memberId\n" +
                    "FROM tbl_member\n" +
                    "ORDER BY memberId DESC LIMIT 1";
//                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.villageId='" + villageId + "'";
            Log.e("getLastInsertedMember", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    member.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return member;
    }

    public ArrayList<Family> searchFamily(String number, String villageId) {
//        number="FM/2009/7339895";
        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.isActive=1 and fm.villageId='" + villageId + "'";
           /* String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.villageId='" + villageId + "'";*/
            Log.e("searchFamily", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    if (cursor.getBlob(cursor.getColumnIndex("photo")) != null) {
                        if (cursor.getBlob(cursor.getColumnIndex("photo")).length > 5) {
                            family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                        } else {

                        }
                    }
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public Member getMemberDetail(String MemberId) {

        Member member = new Member();
        readDatabase();
        try {
            String selectQuery = "select m.memberId,m.familyId,m.emamtafamilyId,m.villageId,m.firstName,m.middleName,m.lastName,m.isHead,m.gender," +
                    "m.maritalStatus,m.birthDate,m.mobileNo,f.houseNumber,m.childof," +
                    "f.faliyaId,f.landmark,f.religionId,f.racialId,f.isBpl,f.lattitudes,f.longitude,f.bplNumber,f.rationcardNrumber," +
                    "f.rsbycardNumber, f.macardNumber, f.anganwadiId " +
                    "from tbl_member as m inner join tbl_family as f on f.emamtafamilyId=m.emamtafamilyId " +
                    "where memberId='" + MemberId + "'";
            Log.v("getMemberDetail", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    member.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    member.setFamilyId(cursor.getString(cursor.getColumnIndex("familyId")));
                    member.setEmamtafamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setVillageId(cursor.getString(cursor.getColumnIndex("villageId")));
                    member.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
                    member.setMiddleName(cursor.getString(cursor.getColumnIndex("middleName")));
                    member.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
                    member.setIsHead(cursor.getString(cursor.getColumnIndex("isHead")));
                    member.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    member.setMaritalStatus(cursor.getString(cursor.getColumnIndex("maritalStatus")));
                    member.setBirthDate(cursor.getString(cursor.getColumnIndex("birthDate")));
                    member.setMobileNo(cursor.getString(cursor.getColumnIndex("mobileNo")));
                    member.setHouseNumber(cursor.getString(cursor.getColumnIndex("houseNumber")));
                    member.setFaliyu(cursor.getString(cursor.getColumnIndex("faliyaId")));
                    member.setLandmark(cursor.getString(cursor.getColumnIndex("landmark")));
                    member.setReligionId(cursor.getString(cursor.getColumnIndex("religionId")));
                    member.setRaciald(cursor.getString(cursor.getColumnIndex("racialId")));
                    member.setIsBpl(cursor.getString(cursor.getColumnIndex("isBpl")));
                    member.setLattitudes(cursor.getString(cursor.getColumnIndex("lattitudes")));
                    member.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                    member.setChildof(cursor.getString(cursor.getColumnIndex("childof")));
                    member.setBplNumber(cursor.getString(cursor.getColumnIndex("bplNumber")));
                    member.setRationcardNrumber(cursor.getString(cursor.getColumnIndex("rationcardNrumber")));
                    member.setRsbycardNumber(cursor.getString(cursor.getColumnIndex("rsbycardNumber")));
                    member.setMacardNumber(cursor.getString(cursor.getColumnIndex("macardNumber")));
                    member.setAnganwadiId(cursor.getString(cursor.getColumnIndex("anganwadiId")));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return member;
    }


    public ArrayList<MaritalStatus> getMaritalStatus() {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.Select_Marital_status));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  maritalstatusId,maritalStatus FROM tbl_maritalStatus";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("maritalstatusId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("maritalStatus")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public ArrayList<MaritalStatus> getRelation() {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.family_head_relation));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  relationwithheadId,relationwithHead FROM tbl_relationwithhead";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("relationwithheadId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("relationwithHead")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public void deleteMemberData() {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.MEMBER_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteHistoryData() {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_HISTORY, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public boolean insertMemberData(JSONArray jsonMemberArray) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(memberId,emamtahealthId,familyId,emamtafamilyId,villageId," +
                    "emrNumber,photo,firstName,middleName,lastName,firstnameEng,middlenameEng,lastnameEng,isHead,relationwithheadId," +
                    "gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod,wanttoadoptfpMethod,plannedfpMethod,isPregnant" +
                    ",wantChild,memberStatus,menstruationStatus,adharcardNumber,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber" +
                    ",migratedemamtafamilyId,migratedemamtamemberId,createdbyuserId,createdDate,updatedDate,subcentreId,isActive," +
                    "migratedvillagId,migrationtypeId,isUpdated,photovalue,bankName,bankbranchName,ifscCode,bankaccountNumber" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(memberId,emamtahealthId,familyId,emamtafamilyId,villageId," +
//                    "emrNumber,photo,firstName,middleName,lastName,firstnameEng,middlenameEng,lastnameEng,isHead,relationwithheadId," +
//                    "gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod,wanttoadoptfpMethod,plannedfpMethod,isPregnant" +
//                    ",wantChild,memberStatus,menstruationStatus,adharcardNumber,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber" +
//                    ",migratedemamtafamilyId,migratedemamtamemberId,createdbyuserId,createdDate,updatedDate" +
//                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonMemberArray.length(); i++) {
                JSONObject jsonObject = jsonMemberArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("memberId"));
                insert.bindString(2, jsonObject.getString("emamtahealthId"));
                insert.bindString(3, jsonObject.getString("familyId"));
                insert.bindString(4, jsonObject.getString("emamtafamilyId"));
                insert.bindString(5, jsonObject.getString("villageId"));
                insert.bindString(6, jsonObject.getString("emrNumber"));
                if (jsonObject.getString("photo") != null) {
                    insert.bindString(7, jsonObject.getString("photo"));
                }
                insert.bindString(8, jsonObject.getString("firstName"));
                insert.bindString(9, jsonObject.getString("middleName"));
                insert.bindString(10, jsonObject.getString("lastName"));
                insert.bindString(11, jsonObject.getString("firstnameEng"));
                insert.bindString(12, jsonObject.getString("middlenameEng"));
                insert.bindString(13, jsonObject.getString("lastnameEng"));
                insert.bindString(14, jsonObject.getString("isHead"));
                insert.bindString(15, jsonObject.getString("relationwithheadId"));
                insert.bindString(16, jsonObject.getString("gender"));
                insert.bindString(17, jsonObject.getString("maritalStatus"));
                insert.bindString(18, jsonObject.getString("birthDate"));
                insert.bindString(19, jsonObject.getString("mobileNo"));
                insert.bindString(20, jsonObject.getString("childof"));
                insert.bindString(21, jsonObject.getString("wifeof"));
                insert.bindString(22, jsonObject.getString("adoptedfpMethod"));
                insert.bindString(23, jsonObject.getString("wanttoadoptfpMethod"));
                insert.bindString(24, jsonObject.getString("plannedfpMethod"));
                insert.bindString(25, jsonObject.getString("isPregnant"));
                insert.bindString(26, jsonObject.getString("wantChild"));
                insert.bindString(27, jsonObject.getString("memberStatus"));
                insert.bindString(28, jsonObject.getString("menstruationStatus"));
                insert.bindString(29, jsonObject.getString("adharcardNumber"));
                insert.bindString(30, jsonObject.getString("electioncardNumber"));
                insert.bindString(31, jsonObject.getString("pancardNumber"));
                insert.bindString(32, jsonObject.getString("drivingcardNumer"));
                insert.bindString(33, jsonObject.getString("passportcardNumber"));
                insert.bindString(34, jsonObject.getString("migratedemamtafamilyId"));
                insert.bindString(35, jsonObject.getString("migratedemamtamemberId"));
                insert.bindString(36, jsonObject.getString("createdbyuserId"));
                insert.bindString(37, jsonObject.getString("createdDate"));
                insert.bindString(38, jsonObject.getString("updatedDate"));
                insert.bindString(39, jsonObject.getString("subcentreId"));
                insert.bindString(40, jsonObject.getString("isActive"));
                insert.bindString(41, jsonObject.getString("migratedvillagId"));
                insert.bindString(42, jsonObject.getString("migrationtypeId"));
                insert.bindString(43, jsonObject.getString("isUpdated"));
                if (!jsonObject.getString("photovalue").equals("null")) {
                    byte[] decodedString = Base64.decode(jsonObject.getString("photovalue"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    String filePath = Utils.saveImageWithName(decodedByte, jsonObject.getString("photo"), Constants.APP_NAME, Constants.PROFILE_PIC);
                    insert.bindString(44, filePath);
                } else {
                    insert.bindString(44, "null");
                }
                insert.bindString(45, jsonObject.getString("bankName"));
                insert.bindString(46, jsonObject.getString("bankbranchName"));
                insert.bindString(47, jsonObject.getString("ifscCode"));
                insert.bindString(48, jsonObject.getString("bankaccountNumber"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertFamilyDetail(JSONArray jsonArray) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.FAMILY_TABLE + "(familyId,emamtafamilyId,villageId,migratedvillagId," +
                    "houseNumber,faliyaId,landmark,racialId,religionId,isBpl,migrationtypeId,bplNumber,rationcardNrumber,rsbycardNumber," +
                    "macardNumber,mavatsalyacardNumber,lattitudes,longitude,createdbyuserId,createdDate,updatedDate,faliyaId,isActive,isUpdated" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("familyId"));
                insert.bindString(2, jsonObject.getString("emamtafamilyId"));
                insert.bindString(3, jsonObject.getString("villageId"));
                insert.bindString(4, jsonObject.getString("migratedvillagId"));
                insert.bindString(5, jsonObject.getString("houseNumber"));
                insert.bindString(6, jsonObject.getString("faliyaId"));
                insert.bindString(7, jsonObject.getString("landmark"));
                insert.bindString(8, jsonObject.getString("racialId"));
                insert.bindString(9, jsonObject.getString("religionId"));
                insert.bindString(10, jsonObject.getString("isBpl"));
                insert.bindString(11, jsonObject.getString("migrationtypeId"));
                insert.bindString(12, jsonObject.getString("bplNumber"));
                insert.bindString(13, jsonObject.getString("rationcardNrumber"));
                insert.bindString(14, jsonObject.getString("rsbycardNumber"));
                insert.bindString(15, jsonObject.getString("macardNumber"));
                insert.bindString(16, jsonObject.getString("mavatsalyacardNumber"));
                insert.bindString(17, jsonObject.getString("lattitudes"));
                insert.bindString(18, jsonObject.getString("longitude"));
                insert.bindString(19, jsonObject.getString("createdbyuserId"));
                insert.bindString(20, jsonObject.getString("createdDate"));
                insert.bindString(21, jsonObject.getString("updatedDate"));
                insert.bindString(22, jsonObject.getString("faliyaId"));
                insert.bindString(23, jsonObject.getString("isActive"));
                insert.bindString(24, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertVillageDetail(JSONArray jsonArray) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.VILLAGE_TABLE + "(villageId,village,subcentreId,talukaId) values (?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("villageId"));
                insert.bindString(2, jsonObject.getString("village"));
                insert.bindString(3, jsonObject.getString("subcentreId"));
                insert.bindString(4, jsonObject.getString("talukaId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public void deleteFamilyDetail() {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.FAMILY_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteVillageDetail() {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.VILLAGE_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteAganwadieDetail() {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.AGANWADI_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public boolean insertFaliyu(String villageId, String faliyaName, String isRisky, String userId) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();
        try {
            String sql = "Insert into " + DBConstant.FALIYA_TABLE + "(villageId,faliyaName,ishighRisk,createdbyuserId,createdDate," +
                    "isUpdated) " +
                    "values (?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, villageId);
            insert.bindString(2, faliyaName);
            insert.bindString(3, isRisky);
            insert.bindString(4, userId);
            insert.bindString(5, dateFormat.format(date));
            insert.bindString(6, "1");
            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean updateFaliya(Religion street) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("faliyaName", street.getName());
            contentValues.put("ishighRisk", street.getIsRisky());
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.FALIYA_TABLE, contentValues, " faliyaId='" + street.getId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean deleteFaliya(Religion street) {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.FALIYA_TABLE, " faliyaId='" + street.getId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<Religion> getFaliyaList(String villageId) {

        ArrayList<Religion> religionArrayList = new ArrayList<>();
        Religion rel = new Religion();
        rel.setId("000");
        rel.setName(mContext.getResources().getString(R.string.street_name));
        religionArrayList.add(rel);
        readDatabase();
        try {
            String selectQuery = "SELECT  * FROM tbl_faliya where villageId='" + villageId + "'";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Religion religion = new Religion();
                    religion.setId(cursor.getString(cursor.getColumnIndex("faliyaId")));
                    religion.setName(cursor.getString(cursor.getColumnIndex("faliyaName")));
                    religion.setIsRisky(cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    religionArrayList.add(religion);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return religionArrayList;
    }


    public ArrayList<Family> searchFamilyMember(String searchString, String strVillageId) {

        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and m.isActive=1 and fm.villageId='" + strVillageId + "'";
            /*String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.villageId='" + strVillageId + "'";*/
            Log.e("searchFamilyMember", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
//                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        family.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        family.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public ArrayList<Family> searchDefaultFamilyMember(String strVillageId) {

        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.isActive=1 and fm.villageId='" + strVillageId + "' LIMIT 10";
            /*String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.villageId='" + strVillageId + "'";*/
            Log.e("searchFamilyMember", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
//                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        family.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        family.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public ArrayList<Family> searchDefaultFamily(String strVillageId) {

        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.isHead='1' and m.isActive=1 and fm.villageId='" + strVillageId + "' LIMIT 10";
            /*String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.villageId='" + strVillageId + "'";*/
            Log.e("searchFamilyMember", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
//                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        family.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        family.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public Member getFamilyMember(String memberId) {

        Member member = new Member();
        readDatabase();
        try {
            String selectQuery = "select m.memberId,m.familyId,m.emamtafamilyId,m.emamtahealthId,m.villageId,m.firstName,m.middleName,m.lastName,m.isHead,m.gender," +
                    "m.maritalStatus,m.birthDate,m.mobileNo,f.houseNumber,m.wifeof,m.childof," +
                    "f.faliyaId,m.photo,f.landmark,f.religionId,f.racialId,f.isBpl,f.lattitudes,f.longitude,f.bplNumber,f.rationcardNrumber," +
                    "f.rsbycardNumber, f.macardNumber,m.adoptedfpMethod,m.wanttoadoptfpMethod,m.plannedfpMethod,m.isPregnant,m.wantChild,m.memberStatus,m.menstruationStatus," +
                    "m.adharcardNumber,m.electioncardNumber,m.pancardNumber,m.drivingcardNumer,m.passportcardNumber,m.relationwithheadId,m.photovalue," +
                    "m.bankName,m.bankbranchName,m.ifscCode,m.bankaccountNumber,adharcardNumber " +
                    "from tbl_member as m inner join tbl_family as f on f.emamtafamilyId=m.emamtafamilyId " +
                    "where memberId='" + memberId + "'";
            Log.v("getFamilyMember", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    member.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    member.setFamilyId(cursor.getString(cursor.getColumnIndex("familyId")));
                    member.setEmamtafamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setVillageId(cursor.getString(cursor.getColumnIndex("villageId")));
                    member.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
                    member.setMiddleName(cursor.getString(cursor.getColumnIndex("middleName")));
                    member.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
                    member.setIsHead(cursor.getString(cursor.getColumnIndex("isHead")));
                    member.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    member.setMaritalStatus(cursor.getString(cursor.getColumnIndex("maritalStatus")));
                    member.setBirthDate(cursor.getString(cursor.getColumnIndex("birthDate")));
                    member.setMobileNo(cursor.getString(cursor.getColumnIndex("mobileNo")));
                    member.setHouseNumber(cursor.getString(cursor.getColumnIndex("houseNumber")));
                    member.setFaliyu(cursor.getString(cursor.getColumnIndex("faliyaId")));
                    member.setLandmark(cursor.getString(cursor.getColumnIndex("landmark")));
                    member.setReligionId(cursor.getString(cursor.getColumnIndex("religionId")));
                    member.setRaciald(cursor.getString(cursor.getColumnIndex("racialId")));
                    member.setIsBpl(cursor.getString(cursor.getColumnIndex("isBpl")));
                    member.setLattitudes(cursor.getString(cursor.getColumnIndex("lattitudes")));
                    member.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                    member.setChildof(cursor.getString(cursor.getColumnIndex("childof")));
                    member.setWifeof(cursor.getString(cursor.getColumnIndex("wifeof")));
                    member.setBplNumber(cursor.getString(cursor.getColumnIndex("bplNumber")));
                    member.setRationcardNrumber(cursor.getString(cursor.getColumnIndex("rationcardNrumber")));
                    member.setRsbycardNumber(cursor.getString(cursor.getColumnIndex("rsbycardNumber")));
                    member.setMacardNumber(cursor.getString(cursor.getColumnIndex("macardNumber")));
                    member.setAdoptedfpMethod(cursor.getString(cursor.getColumnIndex("adoptedfpMethod")));
                    member.setWantadoptedfpMethod(cursor.getString(cursor.getColumnIndex("wanttoadoptfpMethod")));
                    member.setPlannedfpMethod(cursor.getString(cursor.getColumnIndex("plannedfpMethod")));
                    member.setIsPregnant(cursor.getString(cursor.getColumnIndex("isPregnant")));
                    member.setWantChild(cursor.getString(cursor.getColumnIndex("wantChild")));
                    member.setMemberStatus(cursor.getString(cursor.getColumnIndex("memberStatus")));
                    member.setMenstruationStatus(cursor.getString(cursor.getColumnIndex("menstruationStatus")));
                    member.setAdharcardNumber(cursor.getString(cursor.getColumnIndex("adharcardNumber")));
                    member.setElectioncardNumber(cursor.getString(cursor.getColumnIndex("electioncardNumber")));
                    member.setPancardNumber(cursor.getString(cursor.getColumnIndex("pancardNumber")));
                    member.setDrivingcardNumer(cursor.getString(cursor.getColumnIndex("drivingcardNumer")));
                    member.setPassportcardNumber(cursor.getString(cursor.getColumnIndex("passportcardNumber")));
                    member.setRelationwithheadId(cursor.getString(cursor.getColumnIndex("relationwithheadId")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        member.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        member.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    member.setBankName(cursor.getString(cursor.getColumnIndex("bankName")));
                    member.setBranchName(cursor.getString(cursor.getColumnIndex("bankbranchName")));
                    member.setIfscCode(cursor.getString(cursor.getColumnIndex("ifscCode")));
                    member.setAccountNo(cursor.getString(cursor.getColumnIndex("bankaccountNumber")));
                    member.setAadharNo(cursor.getString(cursor.getColumnIndex("adharcardNumber")));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return member;
    }

    public ArrayList<MaritalStatus> getFamilyPlanningData() {
        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.Family_welfare));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  familyplanningmethodId,familyplanningMethod FROM tbl_familyplanningmethod";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("familyplanningmethodId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("familyplanningMethod")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public ArrayList<MaritalStatus> getFamilyUserPlanningData() {
        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.Want_Family_welfare));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  familyplanningmethodId,familyplanningMethod FROM tbl_familyplanningmethod";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("familyplanningmethodId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("familyplanningMethod")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public ArrayList<MaritalStatus> getPeriodeData() {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.select_periods_status));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "SELECT  menstruationstatusId,menstruationStatus FROM tbl_menstruationStatus";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("menstruationstatusId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("menstruationStatus")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public ArrayList<MaritalStatus> getParentList(String emamtaID) {

        ArrayList<MaritalStatus> memberArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.Whos_sun_daughter));
        memberArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "Select emamtahealthId,firstName,middleName,lastName " +
                    "from tbl_member " +
                    "where gender='F' and maritalStatus!='1' and emamtafamilyId='" + emamtaID + "'";

            Log.v("getParentList", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    memberArrayList.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return memberArrayList;
    }

    public ArrayList<MaritalStatus> getWifeList(String emamtaFamilyId) {

        ArrayList<MaritalStatus> memberArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.Whos_wife));
        memberArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "Select emamtahealthId,firstName,middleName,lastName " +
                    "from tbl_member " +
                    "where gender='M' and maritalStatus!='1' and emamtafamilyId='" + emamtaFamilyId + "'";

            Log.v("getWifeList Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    memberArrayList.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return memberArrayList;
    }

    public boolean updateFamilyMemberDetails(Member member) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("firstName", member.getFirstName());
            contentValues.put("middleName", member.getMiddleName());
            contentValues.put("lastName", member.getLastName());
            contentValues.put("isHead", member.getIsHead());
            if (member.getRelationwithheadId() != null) {
                contentValues.put("relationwithheadId", member.getRelationwithheadId());
            }
            contentValues.put("gender", member.getGender());
            contentValues.put("maritalStatus", member.getMaritalStatus());
            if (member.getBirthDate() != null) {
                contentValues.put("birthDate", member.getBirthDate());
            }
            if (member.getMobileNo() != null) {
                contentValues.put("mobileNo", member.getMobileNo());
            }
            if (member.getWifeof() != null) {
                contentValues.put("wifeof", member.getWifeof());
            }
            if (member.getChildof() != null) {
                contentValues.put("childof", member.getChildof());
            }
            if (member.getAdoptedfpMethod() != null) {
                contentValues.put("adoptedfpMethod", member.getAdoptedfpMethod());
            }
            if (member.getWantadoptedfpMethod() != null) {
                contentValues.put("wanttoadoptfpMethod", member.getWantadoptedfpMethod());
            }
            if (member.getPlannedfpMethod() != null) {
                contentValues.put("plannedfpMethod", member.getPlannedfpMethod());
            }
            if (member.getIsPregnant() != null) {
                contentValues.put("isPregnant", member.getIsPregnant());
            }
            if (member.getWantChild() != null) {
                contentValues.put("wantChild", member.getWantChild());
            }
            if (member.getMemberStatus() != null) {
                contentValues.put("memberStatus", member.getMemberStatus());
            }
            if (member.getMenstruationStatus() != null) {
                contentValues.put("menstruationStatus", member.getMenstruationStatus());
            }
            if (member.getElectioncardNumber() != null) {
                contentValues.put("electioncardNumber", member.getElectioncardNumber());
            }
            if (member.getPancardNumber() != null) {
                contentValues.put("pancardNumber", member.getPancardNumber());
            }
            if (member.getDrivingcardNumer() != null) {
                contentValues.put("drivingcardNumer", member.getDrivingcardNumer());
            }
            if (member.getPassportcardNumber() != null) {
                contentValues.put("passportcardNumber", member.getPassportcardNumber());
            }
            contentValues.put("updatedDate", dateFormat.format(date));
//            if (member.getUserImageArray() != null) {
//                contentValues.put("photo", member.getUserImageArray());
//            }
            if (member.getPhoto() != null) {
                contentValues.put("photo", member.getPhoto());
            }
            if (member.getPhotoValue() != null) {
                contentValues.put("photovalue", member.getPhotoValue());
            }
            if (member.getBankName() != null) {
                contentValues.put("bankName", member.getBankName());
            }
            if (member.getBranchName() != null) {
                contentValues.put("bankbranchName", member.getBranchName());
            }
            if (member.getAccountNo() != null) {
                contentValues.put("bankaccountNumber", member.getAccountNo());
            }
            if (member.getIfscCode() != null) {
                contentValues.put("ifscCode", member.getIfscCode());
            }
            if (member.getAadharNo() != null) {
                contentValues.put("adharcardNumber", member.getAadharNo());
            }
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " memberId='" + member.getMemberId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean insertMemberHistory(Member member, String[] historyArray, String[] yeasArray) {

        Member lastMember = getLastInsertedMember();
        deleteHistoryData();
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_HISTORY + "(" +
                    "memberId,historynatureId,sinceDate,sinceYears,createdDate,createdbyuserId,isUpdated,emamtahealthId) " +
                    "values (?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (historyArray != null) {
                for (int i = 0; i < historyArray.length; i++) {
                    if (member.getMemberId() != null) {
                        insert.bindString(1, member.getMemberId());
                    } else {
                        insert.bindString(1, lastMember.getMemberId());
                    }
                    insert.bindString(2, historyArray[i]);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("dd/M/yyyy");
                    int years = Integer.parseInt(yeasArray[i]);
                    c.add(Calendar.YEAR, -years);

                    insert.bindString(3, simpleDateFormat.format(c.getTime()));
                    insert.bindString(4, "" + c.get(Calendar.YEAR));
                    insert.bindString(5, dateFormat.format(date));
                    insert.bindString(6, member.getUserId());
                    insert.bindString(7, "1");
                    insert.bindString(8, member.getEmamtahealthId());
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean deleteHighRisk(String memberId) {
        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_HISTORY, " memberId='" + memberId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean insertNewMember(Member familyMember) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(emamtahealthId,emamtafamilyId,villageId,firstName,middleName," +
                    "lastName,isHead,relationwithheadId,gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod," +
                    "wanttoadoptfpMethod,plannedfpMethod,isPregnant,wantChild,memberStatus,menstruationStatus,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber,createdbyuserId,createdDate,photo,photovalue,isActive," +
                    "bankName,bankbranchName,ifscCode,bankaccountNumber,adharcardNumber,isUpdated" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            insert.bindString(1, familyMember.getEmamtahealthId());
            insert.bindString(2, familyMember.getEmamtafamilyId());
            insert.bindString(3, familyMember.getVillageId());
            insert.bindString(4, familyMember.getFirstName());
            insert.bindString(5, familyMember.getMiddleName());
            insert.bindString(6, familyMember.getLastName());
            insert.bindString(7, familyMember.getIsHead());
            if (familyMember.getRelationwithheadId() != null) {
                insert.bindString(8, familyMember.getRelationwithheadId());
            }

            insert.bindString(9, familyMember.getGender());
            if (familyMember.getMaritalStatus() != null) {
                insert.bindString(10, familyMember.getMaritalStatus());
            }
            if (familyMember.getBirthDate() != null) {
                insert.bindString(11, familyMember.getBirthDate());
            }
            if (familyMember.getMobileNo() != null) {
                insert.bindString(12, familyMember.getMobileNo());
            }
            if (familyMember.getChildof() != null) {
                insert.bindString(13, familyMember.getChildof());
            }
            if (familyMember.getWifeof() != null) {
                insert.bindString(14, familyMember.getWifeof());
            }
            if (familyMember.getAdoptedfpMethod() != null) {
                insert.bindString(15, familyMember.getAdoptedfpMethod());
            }
            if (familyMember.getWantadoptedfpMethod() != null) {
                insert.bindString(16, familyMember.getWantadoptedfpMethod());
            }
            if (familyMember.getPlannedfpMethod() != null) {
                insert.bindString(17, familyMember.getPlannedfpMethod());
            }
            if (familyMember.getIsPregnant() != null) {
                insert.bindString(18, familyMember.getIsPregnant());
            }
            if (familyMember.getWantChild() != null) {
                insert.bindString(19, familyMember.getWantChild());
            }
            if (familyMember.getMemberStatus() != null) {
                insert.bindString(20, familyMember.getMemberStatus());
            }
            if (familyMember.getMenstruationStatus() != null) {
                insert.bindString(21, familyMember.getMenstruationStatus());
            }
            if (familyMember.getElectioncardNumber() != null) {
                insert.bindString(22, familyMember.getElectioncardNumber());
            }
            if (familyMember.getPancardNumber() != null) {
                insert.bindString(23, familyMember.getPancardNumber());
            }
            if (familyMember.getDrivingcardNumer() != null) {
                insert.bindString(24, familyMember.getDrivingcardNumer());
            }
            if (familyMember.getPassportcardNumber() != null) {
                insert.bindString(25, familyMember.getPassportcardNumber());
            }
            insert.bindString(26, familyMember.getUserId());
            insert.bindString(27, dateFormat.format(date));
            if (familyMember.getPhoto() != null) {
                insert.bindString(28, familyMember.getPhoto());
            }
            if (familyMember.getPhotoValue() != null) {
                insert.bindString(29, familyMember.getPhotoValue());
            }
            insert.bindString(30, "1");
            if (familyMember.getBankName() != null) {
                insert.bindString(31, familyMember.getBankName());
            }
            if (familyMember.getBranchName() != null) {
                insert.bindString(32, familyMember.getBranchName());
            }
            if (familyMember.getAccountNo() != null) {
                insert.bindString(33, familyMember.getAccountNo());
            }
            if (familyMember.getIfscCode() != null) {
                insert.bindString(34, familyMember.getIfscCode());
            }
            if (familyMember.getAadharNo() != null) {
                insert.bindString(35, familyMember.getAadharNo());
            }
            insert.bindString(36, "1");
            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean deleteFamily(String eMamtaId) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isActive", "0");
            contentValues.put("isUpdated", "4");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean deleteFamilyMember(String eMamtaId) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isActive", "0");
            contentValues.put("isUpdated", "4");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean deleteMemberByHealthId(String eHealthId) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isActive", "0");
            contentValues.put("isUpdated", "4");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtahealthId='" + eHealthId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<MaritalStatus> getStateData() {

        ArrayList<MaritalStatus> arrayListDistrict = new ArrayList<>();
        readDatabase();
        try {
//            String selectQuery = "select stateId,state from " + DBConstant.TABLE_STATE + " ORDER BY state ASC";
            String selectQuery = "select stateId,state\n" +
                    "from tbl_state\n" +
                    "where stateId=24";

            Log.v("getStateData Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.select_state));
            arrayListDistrict.add(state);

            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("stateId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("state")));
                    arrayListDistrict.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return arrayListDistrict;
    }

    public ArrayList<MaritalStatus> getDistrictData() {

        ArrayList<MaritalStatus> arrayListDistrict = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select districtId,district from tbl_district where stateId=24 ORDER BY district ASC";

            Log.v("getDistrictData Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("123456");
            state.setStatus(mContext.getResources().getString(R.string.dist));
            arrayListDistrict.add(state);

            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("districtId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("district")));
                    arrayListDistrict.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return arrayListDistrict;
    }

    public ArrayList<MaritalStatus> getTaluka(String dirstID) {

        ArrayList<MaritalStatus> arrayListTaluka = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select talukaId,taluka from tbl_taluka where districtId='" + dirstID + "' ORDER BY taluka ASC";

            Log.v("getTaluka Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.taluko));
            arrayListTaluka.add(state);

            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("talukaId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("taluka")));
                    arrayListTaluka.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return arrayListTaluka;
    }

    public ArrayList<MaritalStatus> getVillages(String talukaID) {
        ArrayList<MaritalStatus> arrayListVillages = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select villageId,subcentreId,village from tbl_village where talukaId='" + talukaID + "'";

            Log.v("getTaluka Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.select_village));
            arrayListVillages.add(state);

            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("villageId")) + "," + cursor.getString(cursor.getColumnIndex("subcentreId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("village")));
                    arrayListVillages.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return arrayListVillages;
    }

    public boolean migrateFamilyToState(String eMamtaId, String stateId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedstateId", stateId);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean migrateFamily(String eMamtaId, String villageID, String subcentreId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("villageId", villageID);
            contentValues.put("subcentreId", subcentreId);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "3");
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean migrateFamilyTemp(String eMamtaId, String villageID, String subcentreId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedvillagId", villageID);
            contentValues.put("migratedsubcentreId", subcentreId);
            contentValues.put("migrationtypeId", isParmenant);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "3");
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }


    /*public boolean migrateFamilyMemberToState(String eMamtaId, String eHealthId,String stateId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedemamtafamilyId", eMamtaId);
            contentValues.put("migratedemamtamemberId", eHealthId);
            contentValues.put("migratedstateId", stateId);
            contentValues.put("migrationtypeId", isParmenant);
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtahealthId='" + eHealthId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }*/

    public boolean migrateFamilyMember(String eMamtaId, String eHealthId, String villageID, String subcentreId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedemamtafamilyId", eMamtaId);
            contentValues.put("migratedemamtamemberId", eHealthId);
            contentValues.put("villageId", villageID);
            contentValues.put("subcentreId", subcentreId);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "3");
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean migrateMember(String eMamtaId, String eHealthId, String villageID, String subcentreId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedemamtafamilyId", eMamtaId);
            contentValues.put("migratedemamtamemberId", eHealthId);
            contentValues.put("villageId", villageID);
            contentValues.put("subcentreId", subcentreId);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "3");
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtahealthId='" + eHealthId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean migrateFamilyMemberTemp(String eMamtaId, String eHealthId, String villageID, String subcentreId, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedemamtafamilyId", eMamtaId);
            contentValues.put("migratedemamtamemberId", eHealthId);
            contentValues.put("migratedvillagId", villageID);
            contentValues.put("migratedsubcentreId", subcentreId);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("migrationtypeId", isParmenant);
            contentValues.put("isUpdated", "3");
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtafamilyId='" + eMamtaId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<MaritalStatus> getVillageData() {

        ArrayList<MaritalStatus> villageArray = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select villageId,village from tbl_village";

            Log.v("getVillageData Query", selectQuery);

            MaritalStatus rel = new MaritalStatus();
            rel.setId("000");
            rel.setStatus(mContext.getResources().getString(R.string.village));
            villageArray.add(rel);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("villageId")));
                    member.setStatus(cursor.getString(cursor.getColumnIndex("village")));
                    villageArray.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return villageArray;
    }

    public boolean insertAganwadi(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.AGANWADI_TABLE + "(anganwadiId,talukaId,subcentreId,villageId,anganwadi,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("anganwadiId"));
                insert.bindString(2, jsonObject.getString("talukaId"));
                insert.bindString(3, jsonObject.getString("subcentreId"));
                insert.bindString(4, jsonObject.getString("villageId"));
                insert.bindString(5, jsonObject.getString("anganwadi"));
                insert.bindString(6, jsonObject.getString("createdDate"));
                insert.bindString(7, jsonObject.getString("updatedDate"));
                insert.bindString(8, jsonObject.getString("createdbyuserId"));
                insert.bindString(9, "1");
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertEmployeeDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_EMPLOYEE_PROFILE + "(" +
                    "employeeId,userId,sanctionId,departmentId,designationId,employmentstatusId,employmenttypeId,stateId," +
                    "districtId,talukaId,phcId,subcentreId,villageId,firstName,middleName,lastName,photo,gender,mobileNumber," +
                    "racialId,religionId,birthDate,emailId,employmentstatus,PostHolding,pancardNumber,electioncardNumber,adharcardNumber," +
                    "licensecardNumber,pfNumber,createdbyuserId,createdDate,updatedDate,facilityId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("employeeId"));
                insert.bindString(2, jsonObject.getString("userId"));
                insert.bindString(3, jsonObject.getString("sanctionId"));
                insert.bindString(4, jsonObject.getString("departmentId"));
                insert.bindString(5, jsonObject.getString("designationId"));
                insert.bindString(6, jsonObject.getString("employmentstatusId"));
                insert.bindString(7, jsonObject.getString("employmenttypeId"));
                insert.bindString(8, jsonObject.getString("stateId"));
                insert.bindString(9, jsonObject.getString("districtId"));
                insert.bindString(10, jsonObject.getString("talukaId"));
                insert.bindString(11, jsonObject.getString("phcId"));
                insert.bindString(12, jsonObject.getString("subcentreId"));
                insert.bindString(13, jsonObject.getString("villageId"));
                insert.bindString(14, jsonObject.getString("firstName"));
                insert.bindString(15, jsonObject.getString("middleName"));
                insert.bindString(16, jsonObject.getString("lastName"));
//                insert.bindString(17, jsonObject.getString("photo"));
                insert.bindString(17, "null");
                insert.bindString(18, jsonObject.getString("gender"));
                insert.bindString(19, jsonObject.getString("mobileNumber"));
                insert.bindString(20, jsonObject.getString("racialId"));
                insert.bindString(21, jsonObject.getString("religionId"));
                insert.bindString(22, jsonObject.getString("birthDate"));
                insert.bindString(23, jsonObject.getString("emailId"));
                insert.bindString(24, jsonObject.getString("employmentstatus"));
                insert.bindString(25, jsonObject.getString("PostHolding"));
                insert.bindString(26, jsonObject.getString("pancardNumber"));
                insert.bindString(27, jsonObject.getString("electioncardNumber"));
                insert.bindString(28, jsonObject.getString("adharcardNumber"));
                insert.bindString(29, jsonObject.getString("licensecardNumber"));
                insert.bindString(30, jsonObject.getString("pfNumber"));
                insert.bindString(31, jsonObject.getString("createdbyuserId"));
                insert.bindString(32, jsonObject.getString("createdDate"));
                insert.bindString(33, jsonObject.getString("updatedDate"));
                insert.bindString(34, jsonObject.getString("facilityId"));
                insert.bindString(35, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertChildRegDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_CHILDREG + "(" +
                    "childregdId,emamtahealthId,pregnantwomanregdId,memberId,pregancyoutcomeDate,childregdDate,birthStatus,birthWeight," +
                    "breastFeeding,kangarooCare,corticosteroid,injvitaminK,birthComplication,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("childregdId"));
                insert.bindString(2, jsonObject.getString("emamtahealthId"));
                insert.bindString(3, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(4, jsonObject.getString("memberId"));
                insert.bindString(5, jsonObject.getString("pregancyoutcomeDate"));
                insert.bindString(6, jsonObject.getString("childregdDate"));
                insert.bindString(7, jsonObject.getString("birthStatus"));
                insert.bindString(8, jsonObject.getString("birthWeight"));
                insert.bindString(9, jsonObject.getString("breastFeeding"));
                insert.bindString(10, jsonObject.getString("kangarooCare"));
                insert.bindString(11, jsonObject.getString("corticosteroid"));
                insert.bindString(12, jsonObject.getString("injvitaminK"));
                insert.bindString(13, jsonObject.getString("birthComplication"));
                insert.bindString(14, jsonObject.getString("createdDate"));
                insert.bindString(15, jsonObject.getString("updatedDate"));
                insert.bindString(16, jsonObject.getString("createdbyuserId"));
                insert.bindString(17, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertChildImmunizationDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_CHILDIMMUNIZATION + "(" +
                    "childregdId,emamtahealthId,vaccineName,vaccinedueDate,vaccinegivenDate," +
                    "createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("childregdId"));
                insert.bindString(2, jsonObject.getString("emamtahealthId"));
                insert.bindString(3, jsonObject.getString("vaccineName"));
                insert.bindString(4, jsonObject.getString("vaccinedueDate"));
                insert.bindString(5, jsonObject.getString("vaccinegivenDate"));
                insert.bindString(6, jsonObject.getString("createdDate"));
                insert.bindString(7, jsonObject.getString("updatedDate"));
                insert.bindString(8, jsonObject.getString("createdbyuserId"));
                insert.bindString(9, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertAdoptedMethodDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ADOPTEDMETHOD + "(" +
                    "adoptedfpmethodId,emamtahealthId,emamtafamilyId,villageId,employeeId,facilityId,adoptedfpMethod,copperttypeId," +
                    "adoptedDate,discontinueDate,fpComplication,reasonforremovalId,fpcomplicationDate,statusafterComplication,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("adoptedfpmethodId"));
                insert.bindString(2, jsonObject.getString("emamtahealthId"));
                insert.bindString(3, jsonObject.getString("emamtafamilyId"));
                insert.bindString(4, jsonObject.getString("villageId"));
                insert.bindString(5, jsonObject.getString("employeeId"));
                insert.bindString(6, jsonObject.getString("facilityId"));
                insert.bindString(7, jsonObject.getString("adoptedfpMethod"));
                insert.bindString(8, jsonObject.getString("copperttypeId"));
                insert.bindString(9, jsonObject.getString("adoptedDate"));
                insert.bindString(10, jsonObject.getString("discontinueDate"));
                insert.bindString(11, jsonObject.getString("fpComplication"));
                insert.bindString(12, jsonObject.getString("reasonforremovalId"));
                insert.bindString(13, jsonObject.getString("fpcomplicationDate"));
                insert.bindString(14, jsonObject.getString("statusafterComplication"));
                insert.bindString(15, jsonObject.getString("createdDate"));
                insert.bindString(16, jsonObject.getString("updatedDate"));
                insert.bindString(17, jsonObject.getString("createdbyuserId"));
                insert.bindString(18, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertAncServicedDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ANC_SERVICES + "(" +
                    "ancserviceId,pregnantwomanregdId,ancserviceDate,ancservicetrimesterId,ancserviceTypeId,employeeId,Hb," +
                    "bloodGroup,Weight,Muac,systolicBp,diastolicBp,urinesugar,urinealbumin,rbs,presentation,ifa,calcium,albendazole," +
                    "corticosteroid,hivStatus,vdrlTest,createdDate,updatedDate,createdbyuserId,isUpdated,ancservicedueDate,facilityId) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("ancserviceId"));
                insert.bindString(2, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(3, jsonObject.getString("ancserviceDate"));
                insert.bindString(4, jsonObject.getString("ancservicetrimesterId"));
                insert.bindString(5, jsonObject.getString("ancserviceTypeId"));
                insert.bindString(6, jsonObject.getString("employeeId"));
                insert.bindString(7, jsonObject.getString("Hb"));
                insert.bindString(8, jsonObject.getString("bloodGroup"));
                insert.bindString(9, jsonObject.getString("Weight"));
                insert.bindString(10, jsonObject.getString("Muac"));
                insert.bindString(11, jsonObject.getString("systolicBp"));
                insert.bindString(12, jsonObject.getString("diastolicBp"));
                insert.bindString(13, jsonObject.getString("urinesugar"));
                insert.bindString(14, jsonObject.getString("urinealbumin"));
                insert.bindString(15, jsonObject.getString("rbs"));
                insert.bindString(16, jsonObject.getString("presentation"));
                insert.bindString(17, jsonObject.getString("ifa"));
                insert.bindString(18, jsonObject.getString("calcium"));
                insert.bindString(19, jsonObject.getString("albendazole"));
                insert.bindString(20, jsonObject.getString("corticosteroid"));
                insert.bindString(21, jsonObject.getString("hivStatus"));
                insert.bindString(22, jsonObject.getString("vdrlTest"));
                insert.bindString(23, jsonObject.getString("createdDate"));
                insert.bindString(24, jsonObject.getString("updatedDate"));
                insert.bindString(25, jsonObject.getString("createdbyuserId"));
                insert.bindString(26, jsonObject.getString("isUpdated"));
                insert.bindString(27, jsonObject.getString("ancservicedueDate"));
                insert.bindString(28, jsonObject.getString("facilityId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertAncToxoidDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ANCTETANUSTOXOID + "(" +
                    "ancserviceId,pregnantwomanregdId,tt1dueDate,tt2dueDate,ttboosterdueDate,tt1Date,tt2Date," +
                    "ttboosterDate,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("ancserviceId"));
                insert.bindString(2, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(3, jsonObject.getString("tt1dueDate"));
                insert.bindString(4, jsonObject.getString("tt2dueDate"));
                insert.bindString(5, jsonObject.getString("ttboosterdueDate"));
                insert.bindString(6, jsonObject.getString("tt1Date"));
                insert.bindString(7, jsonObject.getString("tt2Date"));
                insert.bindString(8, jsonObject.getString("ttboosterDate"));
                insert.bindString(9, jsonObject.getString("createdDate"));
                insert.bindString(10, jsonObject.getString("updatedDate"));
                insert.bindString(11, jsonObject.getString("createdbyuserId"));
                insert.bindString(12, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertHighRiskMotherDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_HIGH_RISK_MOTHER + "(" +
                    "highriskmotherId,highriskmothersymptomId,pregnantwomanregdId,ancserviceDate,isReferred,facilityId,facilityName," +
                    "isTreated,treatmentStatus,createdDate,updatedDate,createdbyuserId,isUpdated,ancservicetypeId) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("highriskmotherId"));
                insert.bindString(2, jsonObject.getString("highriskmothersymptomId"));
                insert.bindString(3, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(4, jsonObject.getString("ancserviceDate"));
                insert.bindString(5, jsonObject.getString("isReferred"));
                insert.bindString(6, jsonObject.getString("facilityId"));
                insert.bindString(7, jsonObject.getString("facilityName"));
                insert.bindString(8, jsonObject.getString("isTreated"));
                insert.bindString(9, jsonObject.getString("treatmentStatus"));
                insert.bindString(10, jsonObject.getString("createdDate"));
                insert.bindString(11, jsonObject.getString("updatedDate"));
                insert.bindString(12, jsonObject.getString("createdbyuserId"));
                insert.bindString(13, jsonObject.getString("isUpdated"));
                insert.bindString(14, jsonObject.getString("ancservicetypeId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertFaliyaDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.FALIYA_TABLE + "(" +
                    "faliyaId,villageId,faliyaName,ishighRisk,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("faliyaId"));
                insert.bindString(2, jsonObject.getString("villageId"));
                insert.bindString(3, jsonObject.getString("faliyaName"));
                insert.bindString(4, jsonObject.getString("ishighRisk"));
                insert.bindString(5, jsonObject.getString("createdDate"));
                insert.bindString(6, jsonObject.getString("updatedDate"));
                insert.bindString(7, jsonObject.getString("createdbyuserId"));
                insert.bindString(8, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertPostnatalServiceDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_POSTNATALSERVICES + "(" +
                    "postnatalserviceId,pregnantwomanregdId,postnatalservicetypeId,postnatalservicedueDate," +
                    "postnatalservicegivenDate,postnatalComplication,isReferred,plannedfpMethod,memberStatus," +
                    "createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("postnatalserviceId"));
                insert.bindString(2, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(3, jsonObject.getString("postnatalservicetypeId"));
                insert.bindString(4, jsonObject.getString("postnatalservicedueDate"));
                insert.bindString(5, jsonObject.getString("postnatalservicegivenDate"));
                insert.bindString(6, jsonObject.getString("postnatalComplication"));
                insert.bindString(7, jsonObject.getString("isReferred"));
                insert.bindString(8, jsonObject.getString("plannedfpMethod"));
                insert.bindString(9, jsonObject.getString("memberStatus"));
                insert.bindString(10, jsonObject.getString("createdDate"));
                insert.bindString(11, jsonObject.getString("updatedDate"));
                insert.bindString(12, jsonObject.getString("createdbyuserId"));
                insert.bindString(13, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertPregnantWomenDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_PREGNANT_WOMENREG + "(" +
                    "pregnantwomanregdId,pregnantwomanregdDate,emamtahealthId,employeeId,lmpDate,eddDate,age,gravida,para,abortion," +
                    "livemale,livefemale,livetotal,isearlyancRegd,ishighRisk,isJsy,isKpsy,isCy,facilityId,pregnancyoutcomeDate," +
                    "createdDate,updatedDate,createdbyuserId,isUpdated,weight) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(2, jsonObject.getString("pregnantwomanregdDate"));
                insert.bindString(3, jsonObject.getString("emamtahealthId"));
                insert.bindString(4, jsonObject.getString("employeeId"));
                insert.bindString(5, jsonObject.getString("lmpDate"));
                insert.bindString(6, jsonObject.getString("eddDate"));
                insert.bindString(7, jsonObject.getString("age"));
                insert.bindString(8, jsonObject.getString("gravida"));
                insert.bindString(9, jsonObject.getString("para"));
                insert.bindString(10, jsonObject.getString("abortion"));
                insert.bindString(11, jsonObject.getString("livemale"));
                insert.bindString(12, jsonObject.getString("livefemale"));
                insert.bindString(13, jsonObject.getString("livetotal"));
                insert.bindString(14, jsonObject.getString("isearlyancRegd"));
                insert.bindString(15, jsonObject.getString("ishighRisk"));
                insert.bindString(16, jsonObject.getString("isJsy"));
                insert.bindString(17, jsonObject.getString("isKpsy"));
                insert.bindString(18, jsonObject.getString("isCy"));
                insert.bindString(19, jsonObject.getString("facilityId"));
                insert.bindString(20, jsonObject.getString("pregnancyoutcomeDate"));
                insert.bindString(21, jsonObject.getString("createdDate"));
                insert.bindString(22, jsonObject.getString("updatedDate"));
                insert.bindString(23, jsonObject.getString("createdbyuserId"));
                insert.bindString(24, jsonObject.getString("isUpdated"));
                insert.bindString(25, jsonObject.getString("weight"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertPregnancyOutComeDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_PREGNANCYOUTCOMEREGD + "(" +
                    "pregnancyoutcomeId,pregnantwomanregdId,pregnancyoutcomeregdDate,pregancyoutcometypeId,employeeId,facilityId,facilitynameId," +
                    "pregancyoutcomeDate,pregnancyoutcomeTime,dischargeDate,dischargeTime,numberofChild,deliveryIncentive," +
                    "deliverymamtaKit,delivery108Service,createdDate,updatedDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("pregnancyoutcomeId"));
                insert.bindString(2, jsonObject.getString("pregnantwomanregdId"));
                insert.bindString(3, jsonObject.getString("pregnancyoutcomeregdDate"));
                insert.bindString(4, jsonObject.getString("pregancyoutcometypeId"));
                insert.bindString(5, jsonObject.getString("employeeId"));
                insert.bindString(6, jsonObject.getString("facilityId"));
                insert.bindString(7, jsonObject.getString("facilitynameId"));
                insert.bindString(8, jsonObject.getString("pregancyoutcomeDate"));
                insert.bindString(9, jsonObject.getString("pregnancyoutcomeTime"));
                insert.bindString(10, jsonObject.getString("dischargeDate"));
                insert.bindString(11, jsonObject.getString("dischargeTime"));
                insert.bindString(12, jsonObject.getString("numberofChild"));
                insert.bindString(13, jsonObject.getString("deliveryIncentive"));
                insert.bindString(14, jsonObject.getString("deliverymamtaKit"));
                insert.bindString(15, jsonObject.getString("delivery108Service"));
                insert.bindString(16, jsonObject.getString("createdDate"));
                insert.bindString(17, jsonObject.getString("updatedDate"));
                insert.bindString(18, jsonObject.getString("createdbyuserId"));
                insert.bindString(19, jsonObject.getString("isUpdated"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertMemberHistoryDetails(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_HISTORY + "(" +
                    "memberId,emrNumber,historynatureId,sinceDate,sinceYears,remarks," +
                    "createdDate,updatedDate,createdbyuserId,isUpdated,emamtahealthId) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindString(1, jsonObject.getString("memberId"));
                insert.bindString(2, jsonObject.getString("emrNumber"));
                insert.bindString(3, jsonObject.getString("historynatureId"));
                insert.bindString(4, jsonObject.getString("sinceDate"));
                insert.bindString(5, jsonObject.getString("sinceYears"));
                insert.bindString(6, jsonObject.getString("remarks"));
                insert.bindString(7, jsonObject.getString("createdDate"));
                insert.bindString(8, jsonObject.getString("updatedDate"));
                insert.bindString(9, jsonObject.getString("createdbyuserId"));
                insert.bindString(10, jsonObject.getString("isUpdated"));
                insert.bindString(11, jsonObject.getString("emamtahealthId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public void deleteUserDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.USER_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteEmployeeDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_EMPLOYEE_PROFILE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }


    public void deleteChildRegDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_CHILDREG, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteChildImmunizationDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_CHILDIMMUNIZATION, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteAdoptedMethodsDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_ADOPTEDMETHOD, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteAncServiceDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_ANC_SERVICES, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteAncToxDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_ANCTETANUSTOXOID, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteHighRiskDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_HIGH_RISK_MOTHER, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteFaliyaDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.FALIYA_TABLE, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deletePostnatalDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_POSTNATALSERVICES, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deletePregnantWomenDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_PREGNANT_WOMENREG, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deletePregnancyOutComeDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_PREGNANCYOUTCOMEREGD, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public void deleteMemberHistoryDetails() {

        writeDatabase();
        try {
            mDataBase.delete(DBConstant.TABLE_HISTORY, "1", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public boolean insertUser(JSONArray jsonArray) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.USER_TABLE + "(userId,employeeId,userName,originalPassword,createdDate,updatedDate,createdbyuserId,macId,imeiId) values (?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("userId"));
                insert.bindString(2, jsonObject.getString("employeeId"));
                insert.bindString(3, jsonObject.getString("userName"));
                insert.bindString(4, jsonObject.getString("originalPassword"));
                insert.bindString(5, jsonObject.getString("createdDate"));
                insert.bindString(6, jsonObject.getString("updatedDate"));
                insert.bindString(7, jsonObject.getString("createdbyuserId"));
                insert.bindString(8, jsonObject.getString("macId"));
                insert.bindString(9, jsonObject.getString("imeiId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean getUserDetails(String pin) {
        readDatabase();
        try {
            String selectQuery = "Select userId " +
                    "from tbl_userMaster " +
                    "where originalPassword='" + pin + "'";

            Log.v("getUserDetails Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return false;
    }

    public JSONArray getFaliya() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select faliyaId,villageId,faliyaName,ishighRisk,createdbyuserId,createdDate,updatedDate from tbl_faliya";

            Log.v("getFaliya Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("faliyaId", cursor.getString(cursor.getColumnIndex("faliyaId")));
                    jsonObject.put("villageId", cursor.getString(cursor.getColumnIndex("villageId")));
                    jsonObject.put("faliyaName", cursor.getString(cursor.getColumnIndex("faliyaName")));
                    jsonObject.put("ishighRisk", cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    jsonObject.put("createdbyuserId", cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    jsonObject.put("createdDate", cursor.getString(cursor.getColumnIndex("createdDate")));
                    jsonObject.put("updatedDate", cursor.getString(cursor.getColumnIndex("updatedDate")));
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public boolean storeImage(String filePath) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("photo", filePath);
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtahealthId='A020051203'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public JSONArray familyMamberData() {

        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "Select memberId,photo,photovalue,emamtahealthId,familyId,emamtafamilyId,villageId,employeeId,emrNumber," +
                    "firstName,middleName,lastName,firstnameEng,middlenameEng,lastnameEng,isHead,relationwithheadId,gender,maritalStatus," +
                    "birthDate,mobileNo,childof,wifeof,adoptedfpMethod,wanttoadoptfpMethod,plannedfpMethod,isPregnant,wantChild,memberStatus," +
                    "menstruationStatus,adharcardNumber,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber,migratedemamtafamilyId," +
                    "migratedemamtamemberId,createdbyuserId,createdDate,updatedDate,isActive,subcentreId,migratedvillagId,migrationtypeId," +
                    "isUpdated " +
                    "from tbl_member " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("familyMamberData", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject member = new JSONObject();
                    member.put("memberId", "" + cursor.getString(cursor.getColumnIndex("memberId")));
                    member.put("photo", "" + cursor.getString(cursor.getColumnIndex("photo")));
                    member.put("emamtahealthId", "" + cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.put("familyId", "" + cursor.getString(cursor.getColumnIndex("familyId")));
                    member.put("emamtafamilyId", "" + cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.put("villageId", "" + cursor.getString(cursor.getColumnIndex("villageId")));
                    if (cursor.getString(cursor.getColumnIndex("employeeId")) != null) {
                        member.put("employeeId", cursor.getString(cursor.getColumnIndex("employeeId")));
                    } else {
                        member.put("employeeId", "null");
                    }
                    member.put("emrNumber", "" + cursor.getString(cursor.getColumnIndex("emrNumber")));
                    member.put("firstName", "" + cursor.getString(cursor.getColumnIndex("firstName")));
                    member.put("middleName", "" + cursor.getString(cursor.getColumnIndex("middleName")));
                    member.put("lastName", "" + cursor.getString(cursor.getColumnIndex("lastName")));
                    member.put("firstnameEng", "" + cursor.getString(cursor.getColumnIndex("firstnameEng")));
                    member.put("middlenameEng", "" + cursor.getString(cursor.getColumnIndex("middlenameEng")));
                    member.put("lastnameEng", "" + cursor.getString(cursor.getColumnIndex("lastnameEng")));
                    member.put("isHead", "" + cursor.getString(cursor.getColumnIndex("isHead")));
                    member.put("relationwithheadId", "" + cursor.getString(cursor.getColumnIndex("relationwithheadId")));
                    member.put("gender", "" + cursor.getString(cursor.getColumnIndex("gender")));
                    member.put("maritalStatus", "" + cursor.getString(cursor.getColumnIndex("maritalStatus")));
                    member.put("birthDate", "" + cursor.getString(cursor.getColumnIndex("birthDate")));
                    member.put("mobileNo", "" + cursor.getString(cursor.getColumnIndex("mobileNo")));
                    member.put("childof", "" + cursor.getString(cursor.getColumnIndex("childof")));
                    member.put("wifeof", "" + cursor.getString(cursor.getColumnIndex("wifeof")));
                    member.put("adoptedfpMethod", "" + cursor.getString(cursor.getColumnIndex("adoptedfpMethod")));
                    member.put("wanttoadoptfpMethod", "" + cursor.getString(cursor.getColumnIndex("wanttoadoptfpMethod")));
                    member.put("plannedfpMethod", "" + cursor.getString(cursor.getColumnIndex("plannedfpMethod")));
                    member.put("isPregnant", "" + cursor.getString(cursor.getColumnIndex("isPregnant")));
                    member.put("wantChild", "" + cursor.getString(cursor.getColumnIndex("wantChild")));
                    member.put("memberStatus", "" + cursor.getString(cursor.getColumnIndex("memberStatus")));
                    member.put("menstruationStatus", "" + cursor.getString(cursor.getColumnIndex("menstruationStatus")));
                    member.put("adharcardNumber", "" + cursor.getString(cursor.getColumnIndex("adharcardNumber")));
                    member.put("electioncardNumber", "" + cursor.getString(cursor.getColumnIndex("electioncardNumber")));
                    member.put("pancardNumber", "" + cursor.getString(cursor.getColumnIndex("pancardNumber")));
                    member.put("drivingcardNumer", "" + cursor.getString(cursor.getColumnIndex("drivingcardNumer")));
                    member.put("passportcardNumber", "" + cursor.getString(cursor.getColumnIndex("passportcardNumber")));
                    member.put("migratedemamtafamilyId", "" + cursor.getString(cursor.getColumnIndex("migratedemamtafamilyId")));
                    member.put("migratedemamtamemberId", "" + cursor.getString(cursor.getColumnIndex("migratedemamtamemberId")));
                    member.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    member.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    member.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    member.put("isActive", "" + cursor.getString(cursor.getColumnIndex("isActive")));
                    member.put("subcentreId", "" + cursor.getString(cursor.getColumnIndex("subcentreId")));
                    if (cursor.getString(cursor.getColumnIndex("migratedvillagId")) != null) {
                        member.put("migratedvillagId", cursor.getString(cursor.getColumnIndex("migratedvillagId")));
                    } else {
                        member.put("migratedvillagId", "null");
                    }
                    if (cursor.getString(cursor.getColumnIndex("migrationtypeId")) != null) {
                        member.put("migrationtypeId", cursor.getString(cursor.getColumnIndex("migrationtypeId")));
                    } else {
                        member.put("migrationtypeId", "null");
                    }
                    member.put("isUpdated", cursor.getString(cursor.getColumnIndex("isUpdated")));

                    String File_path = cursor.getString(cursor.getColumnIndex("photovalue"));
                    if (File_path != null) {
                        if (!File_path.equals("null")) {
                            byte[] byteArray = null;
                            try {
                                InputStream inputStream = new FileInputStream(File_path);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                byte[] b = new byte[1024 * 8];
                                int bytesRead = 0;
                                while ((bytesRead = inputStream.read(b)) != -1) {
                                    bos.write(b, 0, bytesRead);
                                }
                                byteArray = bos.toByteArray();
                                String ecodedFile = Base64.encodeToString(byteArray, 0);
                                member.put("photovalue", ecodedFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            member.put("photovalue", "null");
                        }
                    } else {
                        member.put("photovalue", "null");
                    }
                    jsonArray.put(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getAllFamilyMember() {

        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "Select familyId,emamtafamilyId,villageId,migratedvillagId,employeeId,anganwadiId,subcentreId,houseNumber,faliyaId," +
                    "landmark,racialId,religionId,isBpl,migrationtypeId,bplNumber,rationcardNrumber,rsbycardNumber,macardNumber,mavatsalyacardNumber," +
                    "lattitudes,longitude,createdbyuserId,createdDate,updatedDate,isActive,isUpdated,migratedstateId,migratedsubcentreId " +
                    "from tbl_family " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getAllFamilyMember", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject member = new JSONObject();
                    member.put("familyId", cursor.getString(cursor.getColumnIndex("familyId")));
                    member.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.put("villageId", cursor.getString(cursor.getColumnIndex("villageId")));
                    member.put("migratedvillagId", "" + cursor.getString(cursor.getColumnIndex("migratedvillagId")));
                    if (cursor.getString(cursor.getColumnIndex("employeeId")) != null) {
                        member.put("employeeId", cursor.getString(cursor.getColumnIndex("employeeId")));
                    } else {
                        member.put("employeeId", "null");
                    }
                    if (cursor.getString(cursor.getColumnIndex("anganwadiId")) != null) {
                        member.put("anganwadiId", cursor.getString(cursor.getColumnIndex("anganwadiId")));
                    } else {
                        member.put("anganwadiId", "null");
                    }
                    if (cursor.getString(cursor.getColumnIndex("subcentreId")) != null) {
                        member.put("subcentreId", cursor.getString(cursor.getColumnIndex("subcentreId")));
                    } else {
                        member.put("subcentreId", "null");
                    }
                    member.put("houseNumber", cursor.getString(cursor.getColumnIndex("houseNumber")));
                    member.put("faliyaId", cursor.getString(cursor.getColumnIndex("faliyaId")));
                    member.put("landmark", cursor.getString(cursor.getColumnIndex("landmark")));
                    member.put("racialId", cursor.getString(cursor.getColumnIndex("racialId")));
                    member.put("religionId", cursor.getString(cursor.getColumnIndex("religionId")));
                    member.put("isBpl", cursor.getString(cursor.getColumnIndex("isBpl")));
                    member.put("migrationtypeId", cursor.getString(cursor.getColumnIndex("migrationtypeId")));
                    member.put("bplNumber", "" + cursor.getString(cursor.getColumnIndex("bplNumber")));
                    member.put("rationcardNrumber", "" + cursor.getString(cursor.getColumnIndex("rationcardNrumber")));
                    member.put("rsbycardNumber", "" + cursor.getString(cursor.getColumnIndex("rsbycardNumber")));
                    member.put("macardNumber", "" + cursor.getString(cursor.getColumnIndex("macardNumber")));
                    member.put("mavatsalyacardNumber", "" + cursor.getString(cursor.getColumnIndex("mavatsalyacardNumber")));
                    member.put("lattitudes", "" + cursor.getString(cursor.getColumnIndex("lattitudes")));
                    member.put("longitude", "" + cursor.getString(cursor.getColumnIndex("longitude")));
                    member.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    member.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    member.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    member.put("isActive", "" + cursor.getString(cursor.getColumnIndex("isActive")));
                    member.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    member.put("migratedstateId", "" + cursor.getString(cursor.getColumnIndex("migratedstateId")));
                    member.put("migratedsubcentreId", "" + cursor.getString(cursor.getColumnIndex("migratedsubcentreId")));

                    jsonArray.put(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getMemberHistoryDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select Id,memberId,emrNumber,historynatureId,sinceDate,sinceYears,remarks,createdDate,updatedDate,createdbyuserId,isUpdated,emamtahealthId\n" +
                    "from tbl_history " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getMemberHistoryDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject member = new JSONObject();
                    member.put("Id", "" + cursor.getString(cursor.getColumnIndex("Id")));
                    member.put("memberId", "" + cursor.getString(cursor.getColumnIndex("memberId")));
                    member.put("emrNumber", "" + cursor.getString(cursor.getColumnIndex("emrNumber")));
                    member.put("historynatureId", "" + cursor.getString(cursor.getColumnIndex("historynatureId")));
                    member.put("sinceDate", "" + cursor.getString(cursor.getColumnIndex("sinceDate")));
                    member.put("sinceYears", "" + cursor.getString(cursor.getColumnIndex("sinceYears")));
                    member.put("remarks", "" + cursor.getString(cursor.getColumnIndex("remarks")));
                    member.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    member.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    member.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    member.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    member.put("emamtahealthId", "" + cursor.getString(cursor.getColumnIndex("emamtahealthId")));

                    jsonArray.put(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getPregnantWomanDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select pregnantwomanregdId,pregnantwomanregdDate,emamtahealthId,employeeId,lmpDate,eddDate,age,gravida," +
                    "para,abortion,livemale,livefemale,livetotal,isearlyancRegd,ishighRisk,isJsy,isKpsy,isCy,facilityId," +
                    "pregnancyoutcomeDate,createdDate,updatedDate,createdbyuserId,isUpdated,weight\n" +
                    "from tbl_pregnantwomanregd " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getPregnantWomanDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject pregnantWoman = new JSONObject();
                    pregnantWoman.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    pregnantWoman.put("pregnantwomanregdDate", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdDate")));
                    pregnantWoman.put("emamtahealthId", "" + cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    pregnantWoman.put("employeeId", "" + cursor.getString(cursor.getColumnIndex("employeeId")));
                    pregnantWoman.put("lmpDate", "" + cursor.getString(cursor.getColumnIndex("lmpDate")));
                    pregnantWoman.put("eddDate", "" + cursor.getString(cursor.getColumnIndex("eddDate")));
                    pregnantWoman.put("age", "" + cursor.getString(cursor.getColumnIndex("age")));
                    pregnantWoman.put("gravida", "" + cursor.getString(cursor.getColumnIndex("gravida")));
                    pregnantWoman.put("para", "" + cursor.getString(cursor.getColumnIndex("para")));
                    pregnantWoman.put("abortion", "" + cursor.getString(cursor.getColumnIndex("abortion")));
                    pregnantWoman.put("livemale", "" + cursor.getString(cursor.getColumnIndex("livemale")));
                    pregnantWoman.put("livefemale", "" + cursor.getString(cursor.getColumnIndex("livefemale")));
                    pregnantWoman.put("livetotal", "" + cursor.getString(cursor.getColumnIndex("livetotal")));
                    pregnantWoman.put("isearlyancRegd", "" + cursor.getString(cursor.getColumnIndex("isearlyancRegd")));
                    pregnantWoman.put("ishighRisk", "" + cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    pregnantWoman.put("isJsy", "" + cursor.getString(cursor.getColumnIndex("isJsy")));
                    pregnantWoman.put("isKpsy", "" + cursor.getString(cursor.getColumnIndex("isKpsy")));
                    pregnantWoman.put("isCy", "" + cursor.getString(cursor.getColumnIndex("isCy")));
                    pregnantWoman.put("facilityId", "" + cursor.getString(cursor.getColumnIndex("facilityId")));
                    pregnantWoman.put("pregnancyoutcomeDate", "" + cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")));
                    pregnantWoman.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    pregnantWoman.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    pregnantWoman.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    pregnantWoman.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    pregnantWoman.put("weight", "" + cursor.getString(cursor.getColumnIndex("weight")));

                    jsonArray.put(pregnantWoman);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getAncSerivceDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select ancserviceId,pregnantwomanregdId,ancserviceDate,ancservicetrimesterId,ancserviceTypeId,employeeId," +
                    "Hb,bloodGroup,Weight,Muac,systolicBp,diastolicBp,urinesugar,urinealbumin,rbs,presentation,ifa,calcium,albendazole," +
                    "facilityId,corticosteroid,hivStatus,vdrlTest,createdDate,updatedDate,createdbyuserId," +
                    "isUpdated,ancservicedueDate\n" +
                    "from " + DBConstant.TABLE_ANC_SERVICES + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getAncSerivceDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject ancSerivce = new JSONObject();
                    ancSerivce.put("ancserviceId", "" + cursor.getString(cursor.getColumnIndex("ancserviceId")));
                    ancSerivce.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    ancSerivce.put("ancserviceDate", "" + cursor.getString(cursor.getColumnIndex("ancserviceDate")));
                    ancSerivce.put("ancservicetrimesterId", "" + cursor.getString(cursor.getColumnIndex("ancservicetrimesterId")));
                    ancSerivce.put("ancserviceTypeId", "" + cursor.getString(cursor.getColumnIndex("ancserviceTypeId")));
                    ancSerivce.put("employeeId", "" + cursor.getString(cursor.getColumnIndex("employeeId")));
                    ancSerivce.put("Hb", "" + cursor.getString(cursor.getColumnIndex("Hb")));
                    ancSerivce.put("bloodGroup", "" + cursor.getString(cursor.getColumnIndex("bloodGroup")));
                    ancSerivce.put("Weight", "" + cursor.getString(cursor.getColumnIndex("Weight")));
                    ancSerivce.put("Muac", "" + cursor.getString(cursor.getColumnIndex("Muac")));
                    ancSerivce.put("systolicBp", "" + cursor.getString(cursor.getColumnIndex("systolicBp")));
                    ancSerivce.put("diastolicBp", "" + cursor.getString(cursor.getColumnIndex("diastolicBp")));
                    ancSerivce.put("urinesugar", "" + cursor.getString(cursor.getColumnIndex("urinesugar")));
                    ancSerivce.put("urinealbumin", "" + cursor.getString(cursor.getColumnIndex("urinealbumin")));
                    ancSerivce.put("rbs", "" + cursor.getString(cursor.getColumnIndex("rbs")));
                    ancSerivce.put("presentation", "" + cursor.getString(cursor.getColumnIndex("presentation")));
                    ancSerivce.put("ifa", "" + cursor.getString(cursor.getColumnIndex("ifa")));
                    ancSerivce.put("calcium", "" + cursor.getString(cursor.getColumnIndex("calcium")));
                    ancSerivce.put("albendazole", "" + cursor.getString(cursor.getColumnIndex("albendazole")));
                    ancSerivce.put("facilityId", "" + cursor.getString(cursor.getColumnIndex("facilityId")));
                    ancSerivce.put("corticosteroid", "" + cursor.getString(cursor.getColumnIndex("corticosteroid")));
                    ancSerivce.put("hivStatus", "" + cursor.getString(cursor.getColumnIndex("hivStatus")));
                    ancSerivce.put("vdrlTest", "" + cursor.getString(cursor.getColumnIndex("vdrlTest")));
                    ancSerivce.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    ancSerivce.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    ancSerivce.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    ancSerivce.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    ancSerivce.put("ancservicedueDate", "" + cursor.getString(cursor.getColumnIndex("ancservicedueDate")));

                    jsonArray.put(ancSerivce);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getHighRiskDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select highriskmotherId,highriskmothersymptomId,pregnantwomanregdId,ancserviceDate,isReferred,facilityId," +
                    "facilityName,isTreated,treatmentStatus,createdDate,updatedDate,createdbyuserId,isUpdated,ancservicetypeId\n" +
                    "from " + DBConstant.TABLE_HIGH_RISK_MOTHER + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getPregnantWomanDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject ancSerivce = new JSONObject();
                    ancSerivce.put("highriskmotherId", "" + cursor.getString(cursor.getColumnIndex("highriskmotherId")));
                    ancSerivce.put("highriskmothersymptomId", "" + cursor.getString(cursor.getColumnIndex("highriskmothersymptomId")));
                    ancSerivce.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    ancSerivce.put("ancserviceDate", "" + cursor.getString(cursor.getColumnIndex("ancserviceDate")));
                    ancSerivce.put("isReferred", "" + cursor.getString(cursor.getColumnIndex("isReferred")));
                    ancSerivce.put("facilityId", "" + cursor.getString(cursor.getColumnIndex("facilityId")));
                    ancSerivce.put("facilityName", "" + cursor.getString(cursor.getColumnIndex("facilityName")));
                    ancSerivce.put("isTreated", "" + cursor.getString(cursor.getColumnIndex("isTreated")));
                    ancSerivce.put("treatmentStatus", "" + cursor.getString(cursor.getColumnIndex("treatmentStatus")));
                    ancSerivce.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    ancSerivce.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    ancSerivce.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    ancSerivce.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    ancSerivce.put("ancservicetypeId", "" + cursor.getString(cursor.getColumnIndex("ancservicetypeId")));
                    jsonArray.put(ancSerivce);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }


    public JSONArray getAncToxoidDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select ancserviceId,pregnantwomanregdId,tt1dueDate,tt2dueDate,ttboosterdueDate,tt1Date," +
                    "tt2Date,ttboosterDate,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.TABLE_ANCTETANUSTOXOID + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getAncToxoidDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject ancToxoid = new JSONObject();
                    ancToxoid.put("ancserviceId", "" + cursor.getString(cursor.getColumnIndex("ancserviceId")));
                    ancToxoid.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    ancToxoid.put("tt1dueDate", "" + cursor.getString(cursor.getColumnIndex("tt1dueDate")));
                    ancToxoid.put("tt2dueDate", "" + cursor.getString(cursor.getColumnIndex("tt2dueDate")));
                    ancToxoid.put("ttboosterdueDate", "" + cursor.getString(cursor.getColumnIndex("ttboosterdueDate")));
                    ancToxoid.put("tt1Date", "" + cursor.getString(cursor.getColumnIndex("tt1Date")));
                    ancToxoid.put("tt2Date", "" + cursor.getString(cursor.getColumnIndex("tt2Date")));
                    ancToxoid.put("ttboosterDate", "" + cursor.getString(cursor.getColumnIndex("ttboosterDate")));
                    ancToxoid.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    ancToxoid.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    ancToxoid.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    ancToxoid.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(ancToxoid);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getPregOutcomeRegDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select pregnancyoutcomeId,pregnantwomanregdId,pregnancyoutcomeregdDate,pregancyoutcometypeId," +
                    "employeeId,facilityId,facilitynameId,pregancyoutcomeDate,pregnancyoutcomeTime,dischargeDate,dischargeTime," +
                    "numberofChild,deliveryIncentive,deliverymamtaKit,delivery108Service,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.TABLE_PREGNANCYOUTCOMEREGD + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getPregOutcomeRegDetai", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject pregOutCome = new JSONObject();
                    pregOutCome.put("pregnancyoutcomeId", "" + cursor.getString(cursor.getColumnIndex("pregnancyoutcomeId")));
                    pregOutCome.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    pregOutCome.put("pregnancyoutcomeregdDate", "" + cursor.getString(cursor.getColumnIndex("pregnancyoutcomeregdDate")));
                    pregOutCome.put("pregancyoutcometypeId", "" + cursor.getString(cursor.getColumnIndex("pregancyoutcometypeId")));
                    pregOutCome.put("employeeId", "" + cursor.getString(cursor.getColumnIndex("employeeId")));
                    pregOutCome.put("facilityId", "" + cursor.getString(cursor.getColumnIndex("facilityId")));
                    pregOutCome.put("facilitynameId", "" + cursor.getString(cursor.getColumnIndex("facilitynameId")));
                    pregOutCome.put("pregancyoutcomeDate", "" + cursor.getString(cursor.getColumnIndex("pregancyoutcomeDate")));
                    pregOutCome.put("pregnancyoutcomeTime", "" + cursor.getString(cursor.getColumnIndex("pregnancyoutcomeTime")));
                    pregOutCome.put("dischargeDate", "" + cursor.getString(cursor.getColumnIndex("dischargeDate")));
                    pregOutCome.put("dischargeTime", "" + cursor.getString(cursor.getColumnIndex("dischargeTime")));
                    pregOutCome.put("numberofChild", "" + cursor.getString(cursor.getColumnIndex("numberofChild")));
                    pregOutCome.put("deliveryIncentive", "" + cursor.getString(cursor.getColumnIndex("deliveryIncentive")));
                    pregOutCome.put("deliverymamtaKit", "" + cursor.getString(cursor.getColumnIndex("deliverymamtaKit")));
                    pregOutCome.put("delivery108Service", "" + cursor.getString(cursor.getColumnIndex("delivery108Service")));
                    pregOutCome.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    pregOutCome.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    pregOutCome.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    pregOutCome.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(pregOutCome);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getChildRegDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select childregdId,emamtahealthId,pregnantwomanregdId,memberId," +
                    "pregancyoutcomeDate,childregdDate,birthStatus,birthWeight,breastFeeding,kangarooCare,corticosteroid," +
                    "injvitaminK,birthComplication,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.TABLE_CHILDREG + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getChildRegDetails", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject pregOutCome = new JSONObject();
                    pregOutCome.put("childregdId", "" + cursor.getString(cursor.getColumnIndex("childregdId")));
                    pregOutCome.put("emamtahealthId", "" + cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    pregOutCome.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    pregOutCome.put("memberId", "" + cursor.getString(cursor.getColumnIndex("memberId")));
                    pregOutCome.put("pregancyoutcomeDate", "" + cursor.getString(cursor.getColumnIndex("pregancyoutcomeDate")));
                    pregOutCome.put("childregdDate", "" + cursor.getString(cursor.getColumnIndex("childregdDate")));
                    pregOutCome.put("birthStatus", "" + cursor.getString(cursor.getColumnIndex("birthStatus")));
                    pregOutCome.put("birthWeight", "" + cursor.getString(cursor.getColumnIndex("birthWeight")));
                    pregOutCome.put("breastFeeding", "" + cursor.getString(cursor.getColumnIndex("breastFeeding")));
                    pregOutCome.put("kangarooCare", "" + cursor.getString(cursor.getColumnIndex("kangarooCare")));
                    pregOutCome.put("corticosteroid", "" + cursor.getString(cursor.getColumnIndex("corticosteroid")));
                    pregOutCome.put("injvitaminK", "" + cursor.getString(cursor.getColumnIndex("injvitaminK")));
                    pregOutCome.put("birthComplication", "" + cursor.getString(cursor.getColumnIndex("birthComplication")));
                    pregOutCome.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    pregOutCome.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    pregOutCome.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    pregOutCome.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(pregOutCome);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getPostnataServiceDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select postnatalserviceId,pregnantwomanregdId,postnatalservicetypeId,postnatalservicedueDate," +
                    "postnatalservicegivenDate,postnatalComplication,isReferred,plannedfpMethod," +
                    "memberStatus,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.TABLE_POSTNATALSERVICES + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("Query", "getPostnataServiceDetails" + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject postnatalService = new JSONObject();
                    postnatalService.put("postnatalserviceId", "" + cursor.getString(cursor.getColumnIndex("postnatalserviceId")));
                    postnatalService.put("pregnantwomanregdId", "" + cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    postnatalService.put("postnatalservicetypeId", "" + cursor.getString(cursor.getColumnIndex("postnatalservicetypeId")));
                    postnatalService.put("postnatalservicedueDate", "" + cursor.getString(cursor.getColumnIndex("postnatalservicedueDate")));
                    postnatalService.put("postnatalservicegivenDate", "" + cursor.getString(cursor.getColumnIndex("postnatalservicegivenDate")));
                    postnatalService.put("postnatalComplication", "" + cursor.getString(cursor.getColumnIndex("postnatalComplication")));
                    postnatalService.put("isReferred", "" + cursor.getString(cursor.getColumnIndex("isReferred")));
                    postnatalService.put("plannedfpMethod", "" + cursor.getString(cursor.getColumnIndex("plannedfpMethod")));
                    postnatalService.put("memberStatus", "" + cursor.getString(cursor.getColumnIndex("memberStatus")));
                    postnatalService.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    postnatalService.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    postnatalService.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    postnatalService.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(postnatalService);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getChildImmunizationDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select childregdId,emamtahealthId,vaccineName,vaccinedueDate,vaccinegivenDate,hepatitis0givenDate," +
                    "polio0dueDate,polio0givenDate,polio1dueDate,polio1givenDate,pentavalent1dueDate,pentavalent1givenDate,polio2dueDate," +
                    "polio2givenDate,pentavalent2dueDate,pentavalent2givenDate,polio3dueDate,polio3givenDate,pentavalent3dueDate," +
                    "pentavalent3givenDate,measles1dueDate,measles1givenDate,vitamina1dueDate,vitamina1givenDate,polioboosterdueDate," +
                    "dptboosterdueDate,measles2dueDate,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.TABLE_CHILDIMMUNIZATION + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("Query", "getPostnataServiceDetails" + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject childImmunization = new JSONObject();
                    childImmunization.put("childregdId", "" + cursor.getString(cursor.getColumnIndex("childregdId")));
                    childImmunization.put("emamtahealthId", "" + cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    childImmunization.put("vaccineName", "" + cursor.getString(cursor.getColumnIndex("vaccineName")));
                    childImmunization.put("vaccinedueDate", "" + cursor.getString(cursor.getColumnIndex("vaccinedueDate")));
                    childImmunization.put("vaccinegivenDate", "" + cursor.getString(cursor.getColumnIndex("vaccinegivenDate")));
                    childImmunization.put("hepatitis0givenDate", "" + cursor.getString(cursor.getColumnIndex("hepatitis0givenDate")));
                    childImmunization.put("polio0dueDate", "" + cursor.getString(cursor.getColumnIndex("polio0dueDate")));
                    childImmunization.put("polio0givenDate", "" + cursor.getString(cursor.getColumnIndex("polio0givenDate")));
                    childImmunization.put("polio1dueDate", "" + cursor.getString(cursor.getColumnIndex("polio1dueDate")));
                    childImmunization.put("polio1givenDate", "" + cursor.getString(cursor.getColumnIndex("polio1givenDate")));
                    childImmunization.put("pentavalent1dueDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent1dueDate")));
                    childImmunization.put("pentavalent1givenDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent1givenDate")));
                    childImmunization.put("polio2dueDate", "" + cursor.getString(cursor.getColumnIndex("polio2dueDate")));
                    childImmunization.put("polio2givenDate", "" + cursor.getString(cursor.getColumnIndex("polio2givenDate")));
                    childImmunization.put("pentavalent2dueDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent2dueDate")));
                    childImmunization.put("pentavalent2givenDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent2givenDate")));
                    childImmunization.put("polio3dueDate", "" + cursor.getString(cursor.getColumnIndex("polio3dueDate")));
                    childImmunization.put("polio3givenDate", "" + cursor.getString(cursor.getColumnIndex("polio3givenDate")));
                    childImmunization.put("pentavalent3dueDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent3dueDate")));
                    childImmunization.put("pentavalent3givenDate", "" + cursor.getString(cursor.getColumnIndex("pentavalent3givenDate")));
                    childImmunization.put("measles1dueDate", "" + cursor.getString(cursor.getColumnIndex("measles1dueDate")));
                    childImmunization.put("measles1givenDate", "" + cursor.getString(cursor.getColumnIndex("measles1givenDate")));
                    childImmunization.put("vitamina1dueDate", "" + cursor.getString(cursor.getColumnIndex("vitamina1dueDate")));
                    childImmunization.put("vitamina1givenDate", "" + cursor.getString(cursor.getColumnIndex("vitamina1givenDate")));
                    childImmunization.put("polioboosterdueDate", "" + cursor.getString(cursor.getColumnIndex("polioboosterdueDate")));
                    childImmunization.put("dptboosterdueDate", "" + cursor.getString(cursor.getColumnIndex("dptboosterdueDate")));
                    childImmunization.put("measles2dueDate", "" + cursor.getString(cursor.getColumnIndex("measles2dueDate")));
                    childImmunization.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    childImmunization.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    childImmunization.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    childImmunization.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(childImmunization);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }

    public JSONArray getFaliyaDetails() {
        JSONArray jsonArray = new JSONArray();
        readDatabase();
        try {
            String selectQuery = "select faliyaId,villageId,faliyaName,ishighRisk,createdDate,updatedDate,createdbyuserId,isUpdated\n" +
                    "from " + DBConstant.FALIYA_TABLE + " " +
                    "where isUpdated='2' or isUpdated='1' or isUpdated='3' or isUpdated='4'";

            Log.v("getPregOutcomeRegDetai", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject faliya = new JSONObject();
                    faliya.put("faliyaId", "" + cursor.getString(cursor.getColumnIndex("faliyaId")));
                    faliya.put("villageId", "" + cursor.getString(cursor.getColumnIndex("villageId")));
                    faliya.put("faliyaName", "" + cursor.getString(cursor.getColumnIndex("faliyaName")));
                    faliya.put("ishighRisk", "" + cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    faliya.put("createdDate", "" + cursor.getString(cursor.getColumnIndex("createdDate")));
                    faliya.put("updatedDate", "" + cursor.getString(cursor.getColumnIndex("updatedDate")));
                    faliya.put("createdbyuserId", "" + cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    faliya.put("isUpdated", "" + cursor.getString(cursor.getColumnIndex("isUpdated")));
                    jsonArray.put(faliya);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return jsonArray;
    }


    public boolean updateMemberTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateFamilyTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateHistoryTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_HISTORY, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updatePregnantWomanTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_PREGNANT_WOMENREG, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateAncServiceTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_ANC_SERVICES, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateHighRiskTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_HIGH_RISK_MOTHER, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateAncToxoidTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_ANCTETANUSTOXOID, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updatePregOutComeTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_PREGNANCYOUTCOMEREGD, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateChildRegTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_CHILDREG, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updatePostnatalTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_POSTNATALSERVICES, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateChildImmunizationTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.TABLE_CHILDIMMUNIZATION, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateFaliyaTable() {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("isUpdated", "0");
            contentValues.put("updatedDate", dateFormat.format(date));
            mDataBase.update(DBConstant.FALIYA_TABLE, contentValues, null, null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<Member> searchFemales(String searchString, String villageId) {
        ArrayList<Member> womenMemberList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "Select memberId,emamtafamilyId,emamtahealthId,firstName,middleName,lastName,birthDate,photo,photovalue " +
                    "from tbl_member \n" +
                    "where gender='F' and maritalStatus='2' and villageId='" + villageId + "' and firstName LIKE '" + searchString + "%'";

            Log.v("getVillageData Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Member member = new Member();
                    member.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    member.setEmamtafamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
                    member.setMiddleName(cursor.getString(cursor.getColumnIndex("middleName")));
                    member.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
                    member.setBirthDate(cursor.getString(cursor.getColumnIndex("birthDate")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        member.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        member.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    womenMemberList.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenMemberList;
    }

    public Member getPregnantWomenMember(String healthId) {

        Member member = null;
        readDatabase();
        try {
            String selectQuery = "Select m.emamtahealthId,m.memberId,fm.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.birthDate,m.mobileNo,m.photo,m.photovalue,fm.isBpl,fm.racialId," +
                    "bloodGroup,height\n" +
                    "FROM tbl_member m inner join tbl_family fm on m.emamtafamilyId=fm.emamtafamilyId \n" +
                    "where emamtahealthId='" + healthId + "'";

            Log.v("getPregnantWomenMember", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    member = new Member();
                    member.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    member.setEmamtafamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
                    member.setMiddleName(cursor.getString(cursor.getColumnIndex("middleName")));
                    member.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
                    member.setBirthDate(cursor.getString(cursor.getColumnIndex("birthDate")));
                    member.setRaciald(cursor.getString(cursor.getColumnIndex("racialId")));
                    member.setIsBpl(cursor.getString(cursor.getColumnIndex("isBpl")));
                    member.setMobileNo(cursor.getString(cursor.getColumnIndex("mobileNo")));
                    member.setBloodGroup(cursor.getString(cursor.getColumnIndex("bloodGroup")));
                    member.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        member.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        member.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return member;
    }

    public boolean getMemberHivStatus(String emamtahealthId) {

        readDatabase();
        try {
            String selectQuery = "select emamtahealthId,historynatureId\n" +
                    "from tbl_history\n" +
                    "where historynatureId='7' and emamtahealthId='" + emamtahealthId + "'";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    return true;
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return false;
    }

    public ArrayList<WomenHighRisk> getHighRiskCategory() {
        ArrayList<WomenHighRisk> womenHighRiskArrayList = null;
        readDatabase();
        try {
            String selectQuery = "select highriskcategoryId,highriskcategory from tbl_highriskcategory limit 2";

            Log.v("getHighRiskCategory", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                womenHighRiskArrayList = new ArrayList<>();
                do {
                    WomenHighRisk womenHighRisk = new WomenHighRisk();
                    womenHighRisk.setCategoryId(cursor.getString(cursor.getColumnIndex("highriskcategoryId")));
                    womenHighRisk.setCategoryName(cursor.getString(cursor.getColumnIndex("highriskcategory")));
                    womenHighRiskArrayList.add(womenHighRisk);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenHighRiskArrayList;
    }

    public ArrayList<WomenHighRisk> getHighRiskSymtoms(ArrayList<WomenHighRisk> womenHighRiskArray) {
        ArrayList<HighRiskSymtoms> highRiskSymtomses = null;
        readDatabase();
        try {
            String selectQuery = "select highriskcategoryId,highriskmothersymptomId,highriskmotherSymptom from tbl_highriskmothersymptom";

            Log.v("getHighRiskSymtoms", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                for (int j = 0; j < womenHighRiskArray.size(); j++) {
                    cursor.moveToFirst();
                    highRiskSymtomses = new ArrayList<>();
                    WomenHighRisk womenHighRisk = womenHighRiskArray.get(j);
//                    do {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (womenHighRisk.getCategoryId().equals(cursor.getString(cursor.getColumnIndex("highriskcategoryId")))) {
                            HighRiskSymtoms highRiskSymtoms = new HighRiskSymtoms();
                            highRiskSymtoms.setCategoryId(cursor.getString(cursor.getColumnIndex("highriskcategoryId")));
                            highRiskSymtoms.setSymptomId(cursor.getString(cursor.getColumnIndex("highriskmothersymptomId")));
                            highRiskSymtoms.setSymptomName(cursor.getString(cursor.getColumnIndex("highriskmotherSymptom")));
                            highRiskSymtoms.setIsChecked("0");
                            highRiskSymtomses.add(highRiskSymtoms);
                            womenHighRisk.setHighRiskSymtomsArrayList(highRiskSymtomses);
                        }
                        cursor.moveToNext();
                    }
                    womenHighRiskArray.set(j, womenHighRisk);

//                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenHighRiskArray;
    }

    public boolean insertNewPagnentWomen(PregnantWomen pregnantWomen) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_PREGNANT_WOMENREG + "(" +
                    "pregnantwomanregdId,pregnantwomanregdDate,lmpDate,eddDate,age,gravida,para,livemale,livefemale,livetotal,abortion," +
                    "emamtahealthId,ishighRisk,isJsy,isKpsy,isCy,employeeId,createdDate,createdbyuserId,isUpdated,weight) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, pregnantWomen.getPregnantwomanregdId());
            insert.bindString(2, pregnantWomen.getAncDate());
            insert.bindString(3, pregnantWomen.getLmpDate());
            insert.bindString(4, pregnantWomen.getEddDate());
            insert.bindString(5, pregnantWomen.getAge());
            insert.bindLong(6, pregnantWomen.getGravida());
            insert.bindLong(7, pregnantWomen.getPara());
            insert.bindLong(8, pregnantWomen.getMale());
            insert.bindLong(9, pregnantWomen.getFemale());
            insert.bindLong(10, pregnantWomen.getMale() + pregnantWomen.getFemale());
            insert.bindLong(11, pregnantWomen.getAbortion());
            insert.bindString(12, pregnantWomen.getEmamtahealthId());
            insert.bindString(13, pregnantWomen.getIsHighRisk());
            insert.bindString(14, pregnantWomen.getIsJsy());
            insert.bindString(15, pregnantWomen.getIsKpsy());
            insert.bindString(16, pregnantWomen.getIsChiranjivi());
            if (pregnantWomen.getEmployeeId() != null) {
                insert.bindString(17, pregnantWomen.getEmployeeId());
            }
            insert.bindString(18, dateFormat.format(date));
            insert.bindString(19, pregnantWomen.getUserId());
            insert.bindString(20, "1");
            if (pregnantWomen.getWeight() != null) {
                insert.bindString(21, pregnantWomen.getWeight());
            }
            insert.execute();

            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertHighRiskPregnantWomen(PregnantWomen pregnantWomen) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_HIGH_RISK_MOTHER + "(" +
                    "highriskmothersymptomId,pregnantwomanregdId,ancserviceDate,createdDate,createdbyuserId,ancservicetypeId,isUpdated) " +
                    "values (?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (pregnantWomen.getSymptomsId() != null) {
                for (int i = 0; i < pregnantWomen.getSymptomsId().length; i++) {
                    insert.bindString(1, pregnantWomen.getSymptomsId()[i]);
                    insert.bindString(2, pregnantWomen.getPregnantwomanregdId());
                    insert.bindString(3, pregnantWomen.getAncDate());
                    insert.bindString(4, dateFormat.format(date));
                    insert.bindString(5, pregnantWomen.getUserId());
                    insert.bindString(6, "0");
                    insert.bindString(7, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertAncServices(PregnantWomen pregnantWomen) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ANC_SERVICES + "(" +
                    "pregnantwomanregdId,ancservicedueDate,ancservicetrimesterId,createdDate,isUpdated) " +
                    "values (?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (pregnantWomen.getAncServices() != null) {
                int j = 0;
                for (int i = 0; i < pregnantWomen.getAncServices().length; i++) {
                    insert.bindString(1, pregnantWomen.getPregnantwomanregdId());
                    insert.bindString(2, pregnantWomen.getAncServices()[i]);
                    j = j + 1;
                    insert.bindLong(3, j);
                    insert.bindString(4, dateFormat.format(date));
                    insert.bindString(5, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public ArrayList<MaritalStatus> getAshaWorkers(String villageId) {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();

        MaritalStatus rel = new MaritalStatus();
        rel.setId("000");
        rel.setStatus(mContext.getResources().getString(R.string.select_asha_name));
        maritalStatusArrayList.add(rel);

        readDatabase();
        try {
            String selectQuery = "select employeeId,firstName,middleName,lastName\n" +
                    "from tbl_employeeprofile\n" +
                    "where facilityId='101' and designationId='25' and villageId='" + villageId + "'";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("employeeId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public boolean updatePregnantWomanFamily(PregnantWomen pregnantWomen) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            if (pregnantWomen.getIsBpl() != null) {
                contentValues.put("isBpl", pregnantWomen.getIsBpl());
            }
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + pregnantWomen.getEmamtaId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updatePregnantWomanMember(PregnantWomen pregnantWomen) {

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            if (pregnantWomen.getMobile() != null) {
                contentValues.put("mobileNo", pregnantWomen.getMobile());
            }
            contentValues.put("updatedDate", dateFormat.format(date));
            if (pregnantWomen.getPhoto() != null) {
                contentValues.put("photo", pregnantWomen.getPhoto());
            }
            if (pregnantWomen.getPhotoValue() != null) {
                contentValues.put("photovalue", pregnantWomen.getPhotoValue());
            }
            if (pregnantWomen.getHeight() != null) {
                contentValues.put("height", pregnantWomen.getHeight());
            }
            if (pregnantWomen.getBloodGroup() != null) {
                contentValues.put("bloodGroup", pregnantWomen.getBloodGroup());
            }
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.MEMBER_TABLE, contentValues, " emamtahealthId='" + pregnantWomen.getMemberId() + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<PregnantWomen> searchWomanForHealthService(String searchString, String villageId) {
        ArrayList<PregnantWomen> womenMemberList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "Select m.emamtahealthId,fm.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue,pw.pregnantwomanregdId,pw.ishighRisk,pw.pregnantwomanregdDate,pw.eddDate,m.bloodGroup\n" +
                    "FROM tbl_pregnantwomanRegd pw\n" +
                    "inner join tbl_member m  on pw.emamtahealthId = m.emamtahealthId \n" +
                    "inner join tbl_family fm on m.emamtafamilyId=fm.emamtafamilyId\n" +
                    "where pw.pregnancyoutcomeDate is null and fm.villageId='" + villageId + "' and m.firstName LIKE '" + searchString + "%' \n" +
                    "ORDER BY pw.pregnantwomanregdDate Asc";

            Log.e(" Query", "searchWomanForHealthService = " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PregnantWomen member = new PregnantWomen();
                    member.setEmamtaId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        member.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        member.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    member.setPregnantwomanregdId(cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    member.setIsHighRisk(cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    member.setPregnantwomanregdDate(cursor.getString(cursor.getColumnIndex("pregnantwomanregdDate")));
                    member.setEddDate(cursor.getString(cursor.getColumnIndex("eddDate")));
                    member.setBloodGroup(cursor.getString(cursor.getColumnIndex("bloodGroup")));

                    womenMemberList.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenMemberList;
    }

    public boolean checkPregnantWoman(String emamtahealthId) {

        readDatabase();
        try {
            String selectQuery = "select emamtahealthId,pregnancyoutcomeDate\n" +
                    "from tbl_pregnantwomanRegd\n" +
                    "where emamtahealthId='" + emamtahealthId + "' and pregnancyoutcomeDate is null";

            Log.e("checkPregnantWoman", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    return true;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return false;
    }

    public ArrayList<PregnantWomen> getPregnantWoman(String pregRegdId) {

        ArrayList<PregnantWomen> womenMemberList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select m.emamtahealthId,pw.pregnantwomanregdId,pw.pregnantwomanregdDate,m.firstName,m.middleName,m.lastName,anc.ancservicedueDate,anc.ancserviceDate," +
                    "pw.lmpDate,pw.eddDate\n" +
                    "from tbl_pregnantwomanRegd pw\n" +
                    "inner join tbl_ancService anc  on pw.pregnantwomanregdId = anc.pregnantwomanregdId\n" +
                    "inner join tbl_member m on m.emamtahealthId=pw.emamtahealthId\n" +
                    "where pw.pregnantwomanregdId='" + pregRegdId + "'\n" +
                    "Order by anc.ancservicetrimesterId Asc";

            Log.e("getPregnantWoman Query", "" + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("dd/M/yyyy");
            if (cursor.moveToFirst()) {
                do {
                    PregnantWomen member = new PregnantWomen();
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setPregnantwomanregdId(cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    member.setPregnantwomanregdDate(cursor.getString(cursor.getColumnIndex("pregnantwomanregdDate")));
                    member.setAncServiceDate(cursor.getString(cursor.getColumnIndex("ancserviceDate")));
                    member.setAncServiceDueDate(cursor.getString(cursor.getColumnIndex("ancservicedueDate")));
                    member.setLmpDate(cursor.getString(cursor.getColumnIndex("lmpDate")));
                    member.setEddDate(cursor.getString(cursor.getColumnIndex("eddDate")));

                    Date ServiceDueDate = simpleDateFormat.parse(member.getAncServiceDueDate());
                    Date PregnantwomanregdDate = simpleDateFormat.parse(member.getPregnantwomanregdDate());
                    Date EddDate = simpleDateFormat.parse(member.getEddDate());

                    boolean isValid = Utils.between(ServiceDueDate, PregnantwomanregdDate, EddDate);
                    if (isValid) {
                        womenMemberList.add(member);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenMemberList;
    }

    public ArrayList<WomenHighRisk> getHistoryType() {
        ArrayList<WomenHighRisk> womenHighRiskArrayList = null;
        readDatabase();
        try {
            String selectQuery = "select historytypeId,historyType1 from tbl_historytype";

            Log.v("getHistoryType", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                womenHighRiskArrayList = new ArrayList<>();
                do {
                    WomenHighRisk womenHighRisk = new WomenHighRisk();
                    womenHighRisk.setCategoryId(cursor.getString(cursor.getColumnIndex("historytypeId")));
                    womenHighRisk.setCategoryName(cursor.getString(cursor.getColumnIndex("historyType1")));
                    womenHighRiskArrayList.add(womenHighRisk);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenHighRiskArrayList;
    }

    public ArrayList<WomenHighRisk> getMemberHistory(ArrayList<WomenHighRisk> womenHighRiskArray) {
        ArrayList<HighRiskSymtoms> highRiskSymtomses = null;
        readDatabase();
        try {
            String selectQuery = "select historynatureId,historytypeId,historyNature1 from tbl_historynature";

            Log.v("getMemberHistory", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                for (int j = 0; j < womenHighRiskArray.size(); j++) {
                    cursor.moveToFirst();
                    highRiskSymtomses = new ArrayList<>();
                    WomenHighRisk womenHighRisk = womenHighRiskArray.get(j);
//                    do {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (womenHighRisk.getCategoryId().equals(cursor.getString(cursor.getColumnIndex("historytypeId")))) {
                            HighRiskSymtoms highRiskSymtoms = new HighRiskSymtoms();
                            highRiskSymtoms.setCategoryId(cursor.getString(cursor.getColumnIndex("historytypeId")));
                            highRiskSymtoms.setSymptomId(cursor.getString(cursor.getColumnIndex("historynatureId")));
                            highRiskSymtoms.setSymptomName(cursor.getString(cursor.getColumnIndex("historyNature1")));
                            highRiskSymtoms.setIsChecked("0");
                            highRiskSymtomses.add(highRiskSymtoms);
                            womenHighRisk.setHighRiskSymtomsArrayList(highRiskSymtomses);
                        }
                        cursor.moveToNext();
                    }
                    womenHighRiskArray.set(j, womenHighRisk);

//                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenHighRiskArray;
    }

    public ArrayList<HighRiskSymtoms> getMemberExistHistory(String memberId) {
        ArrayList<HighRiskSymtoms> highRiskSymtomses = null;
        readDatabase();
        try {
            String selectQuery = "select memberId,emamtahealthId,th.historynatureId,th.sinceYears\n" +
                    "from tbl_history as th\n" +
                    "inner join tbl_historynature as thn on th.historynatureId=thn.historynatureId\n" +
                    "inner join tbl_historytype as tht on thn.historytypeId=tht.historytypeId\n" +
                    "where th.memberId='" + memberId + "'";

            Log.v("getMemberHistory", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                highRiskSymtomses = new ArrayList<>();
                do {
                    HighRiskSymtoms highRiskSymtoms = new HighRiskSymtoms();
                    highRiskSymtoms.setSymptomId(cursor.getString(cursor.getColumnIndex("historynatureId")));
                    highRiskSymtoms.setYear(cursor.getString(cursor.getColumnIndex("sinceYears")));
                    highRiskSymtomses.add(highRiskSymtoms);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return highRiskSymtomses;
    }

    public ArrayList<WomenHighRisk> getMemberHistoryDetail(ArrayList<WomenHighRisk> womenHighRiskArray, ArrayList<HighRiskSymtoms> highRiskSymtomsArrayList) {
        ArrayList<HighRiskSymtoms> highRiskSymtomses = null;
        readDatabase();
        try {
            String selectQuery = "select historynatureId,historytypeId,historyNature1 from tbl_historynature";

            Log.v("getMemberHistory", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                for (int j = 0; j < womenHighRiskArray.size(); j++) {
                    cursor.moveToFirst();
                    highRiskSymtomses = new ArrayList<>();
                    WomenHighRisk womenHighRisk = womenHighRiskArray.get(j);
//                    do {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        if (womenHighRisk.getCategoryId().equals(cursor.getString(cursor.getColumnIndex("historytypeId")))) {
                            for (int p = 0; p < highRiskSymtomsArrayList.size(); p++) {
                                if (cursor.getString(cursor.getColumnIndex("historynatureId")).equals(highRiskSymtomsArrayList.get(p).getSymptomId())) {
                                    HighRiskSymtoms highRiskSymtoms = new HighRiskSymtoms();

                                    Calendar currentDate = Calendar.getInstance();
                                    int fromYear = currentDate.get(Calendar.YEAR);
                                    int toYear = Integer.parseInt(highRiskSymtomsArrayList.get(p).getYear());
                                    int year = fromYear - toYear;

                                    highRiskSymtoms.setCategoryId(cursor.getString(cursor.getColumnIndex("historytypeId")));
                                    highRiskSymtoms.setSymptomId(cursor.getString(cursor.getColumnIndex("historynatureId")));
                                    highRiskSymtoms.setSymptomName(cursor.getString(cursor.getColumnIndex("historyNature1")));
                                    highRiskSymtoms.setIsChecked("1");
                                    highRiskSymtoms.setYear("" + year);
                                    highRiskSymtomses.add(highRiskSymtoms);
                                    womenHighRisk.setHighRiskSymtomsArrayList(highRiskSymtomses);
                                    womenHighRiskArray.set(j, womenHighRisk);
                                } else {
                                    HighRiskSymtoms highRiskSymtoms = new HighRiskSymtoms();
                                    highRiskSymtoms.setCategoryId(cursor.getString(cursor.getColumnIndex("historytypeId")));
                                    highRiskSymtoms.setSymptomId(cursor.getString(cursor.getColumnIndex("historynatureId")));
                                    highRiskSymtoms.setSymptomName(cursor.getString(cursor.getColumnIndex("historyNature1")));
                                    highRiskSymtoms.setIsChecked("0");
                                    highRiskSymtoms.setYear(null);
                                    highRiskSymtomses.add(highRiskSymtoms);
                                    womenHighRisk.setHighRiskSymtomsArrayList(highRiskSymtomses);
                                    womenHighRiskArray.set(j, womenHighRisk);
                                }

                            }

                        }
                        cursor.moveToNext();
                    }


//                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenHighRiskArray;
    }

    public boolean setAncServiceData(AntenatalService antenatalService) {
        writeDatabase();
        try {
//            String sql = "Insert into " + DBConstant.TABLE_ANC_SERVICES + "(" +
//                    "pregnancywomanregdId,ancserviceDate,ancservicetrimesterId,ancserviceTypeId,Hb,bloodGroup," +
//                    "Weight,Muac,systolicBp,diastolicBp,urinesugar,urinealbumin,rbs,presentation,ifa,calcium,albendazole," +
//                    "corticosteroid,hivStatus,vdrlTest,tt1Date,tt2Date,ttboosterDate,createdDate,createdbyuserId) " +
//                    "values (?,?,?,?,?,?)";

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ancserviceDate", antenatalService.getAncServicedate());
            contentValues.put("Hb", antenatalService.getHb());
            if (antenatalService.getBloodGroup() != null) {
                contentValues.put("bloodGroup", antenatalService.getBloodGroup());
            }
            contentValues.put("Weight", antenatalService.getWeight());
            contentValues.put("Muac", antenatalService.getMuac());
            contentValues.put("systolicBp", antenatalService.getSystolicbp());
            contentValues.put("diastolicBp", antenatalService.getDiastolicbp());
            contentValues.put("urinesugar", antenatalService.getUrinesusugar());
            contentValues.put("urinealbumin", antenatalService.getUrinealbumin());
            contentValues.put("rbs", antenatalService.getRbs());
            contentValues.put("presentation", antenatalService.getPresentation());
            contentValues.put("ifa", antenatalService.getIfaTablet());
            contentValues.put("calcium", antenatalService.getCalcium());
            contentValues.put("albendazole", antenatalService.getAlbendazole());
            contentValues.put("corticosteroid", antenatalService.getCorticoSteroid());
            contentValues.put("hivStatus", antenatalService.getHiv());
            contentValues.put("vdrlTest", antenatalService.getVdrl());

            contentValues.put("isUpdated", "2");
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("ancserviceTypeId", antenatalService.getAncServicetype());
            mDataBase.update(DBConstant.TABLE_ANC_SERVICES, contentValues, " ancservicetrimesterId='" + antenatalService.getAncservicetrimesterId() + "'", null);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean setHighRiskData(ArrayList<String> arrayList, AntenatalService antenatalService) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_HIGH_RISK_MOTHER + "(" +
                    "highriskmothersymptomId,pregnantwomanregdId,ancserviceDate,createdDate,createdbyuserId,ancservicetypeId,isUpdated) " +
                    "values (?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (arrayList.size() != 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    insert.bindString(1, arrayList.get(i));
                    insert.bindString(2, antenatalService.getPregnantwomanregdId());
                    insert.bindString(3, antenatalService.getAncServicedate());
                    insert.bindString(4, dateFormat.format(date));
                    insert.bindString(5, antenatalService.getUserId());
                    insert.bindString(6, "0");
                    insert.bindString(7, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean setHighRisk(ArrayList<String> arrayList, AntenatalService antenatalService) {
        writeDatabase();
        try {
            if (arrayList.size() != 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                ContentValues contentValues = new ContentValues();
                contentValues.put("ishighRisk", "1");
                contentValues.put("updatedDate", dateFormat.format(date));
                contentValues.put("isUpdated", "2");
                mDataBase.update(DBConstant.TABLE_PREGNANT_WOMENREG, contentValues, " pregnantwomanregdId='" + antenatalService.getPregnantwomanregdId() + "'", null);
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean insertTTDate(AntenatalService antenatalService) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ANCTETANUSTOXOID + "(" +
                    "pregnantwomanregdId,tt1Date,tt1dueDate,tt2dueDate,ttboosterdueDate,tt2Date,ttboosterDate," +
                    "createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
//
            insert.bindString(1, antenatalService.getPregnantwomanregdId());
            if (antenatalService.getTt1().length() != 0) {
                insert.bindString(2, antenatalService.getTt1());
                insert.bindString(3, antenatalService.getAncServicedate());
                insert.bindString(4, antenatalService.getTt2dueDate());
                insert.bindString(5, antenatalService.getAncServicedate());
            }
            if (antenatalService.getTt2().length() != 0) {
                insert.bindString(6, antenatalService.getTt2());
            }
            if (antenatalService.getTt_booster().length() != 0) {
                insert.bindString(7, antenatalService.getTt_booster());
            }
            ;
            insert.bindString(8, dateFormat.format(date));
            insert.bindString(9, antenatalService.getUserId());
            insert.bindString(10, "1");
            insert.execute();

            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean isServiceExist(int position) {
        readDatabase();
        try {
            String selectQuery = "select pregnantwomanregdId,ancserviceDate\n" +
                    "from tbl_ancservice\n" +
                    "where ancserviceTypeId='" + position + "'";

            Log.e("isServiceExist", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    return true;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return false;
    }

    public ArrayList<String> checkServiceDate(int selectedItemPosition) {
        readDatabase();
        ArrayList<String> dateArrayList = null;
        try {
            String selectQuery = "select ancserviceDate,ancserviceTypeId\n" +
                    "from tbl_ancservice\n" +
                    "where ancserviceTypeId is  not null";

            Log.e("checkServiceDate", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.getCount() != 0) {
                dateArrayList = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        dateArrayList.add(cursor.getString(cursor.getColumnIndex("ancserviceDate")));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return dateArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return dateArrayList;
    }

    public ArrayList<String> checkTypeId() {
        readDatabase();
        ArrayList<String> dateArrayList = null;
        try {
            String selectQuery = "select ancserviceDate,ancserviceTypeId,ancservicetrimesterId\n" +
                    "from tbl_ancservice\n" +
                    "where ancserviceTypeId is  not null";

            Log.e("checkServiceDate", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.getCount() != 0) {
                dateArrayList = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {

                        dateArrayList.add(cursor.getString(cursor.getColumnIndex("ancservicetrimesterId")));
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return dateArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return dateArrayList;
    }

    public ArrayList<PregnantWomen> searchWomanForPregRegister(String searchString, String villageId) {
        ArrayList<PregnantWomen> womenMemberList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "Select m.emamtahealthId,fm.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue,pw.pregnantwomanregdId,pw.ishighRisk,pw.pregnantwomanregdDate,pw.eddDate,m.bloodGroup\n" +
                    "FROM tbl_pregnantwomanRegd pw\n" +
                    "inner join tbl_member m  on pw.emamtahealthId = m.emamtahealthId \n" +
                    "inner join tbl_family fm on m.emamtafamilyId=fm.emamtafamilyId\n" +
                    "where pw.pregnancyoutcomeDate is null and fm.villageId='" + villageId + "' and m.firstName LIKE '" + searchString + "%' \n" +
                    "ORDER BY pw.pregnantwomanregdDate Asc";

            Log.e(" Query", "searchWomanForPregRegister = " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    PregnantWomen member = new PregnantWomen();
                    member.setEmamtaId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.setEmamtahealthId(cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.setName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        member.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                        member.setPhotoValue(cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    member.setPregnantwomanregdId(cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    member.setIsHighRisk(cursor.getString(cursor.getColumnIndex("ishighRisk")));
                    member.setPregnantwomanregdDate(cursor.getString(cursor.getColumnIndex("pregnantwomanregdDate")));
                    member.setEddDate(cursor.getString(cursor.getColumnIndex("eddDate")));
                    member.setBloodGroup(cursor.getString(cursor.getColumnIndex("bloodGroup")));
                    womenMemberList.add(member);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return womenMemberList;
    }

    public ArrayList<MaritalStatus> getFacilityData() {

        readDatabase();
        ArrayList<MaritalStatus> facilityArrayList = new ArrayList<>();
        try {
            String Query = "select facilityId,facility1\n" +
                    "from tbl_facility\n" +
                    "where facility1!=''";
            Cursor cursor = mDataBase.rawQuery(Query, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.select_delivery_place));
            facilityArrayList.add(state);

            if (cursor.moveToFirst()) {

                do {
                    MaritalStatus facility = new MaritalStatus();
                    facility.setId(cursor.getString(cursor.getColumnIndex("facilityId")));
                    facility.setStatus(cursor.getString(cursor.getColumnIndex("facility1")));
                    facilityArrayList.add(facility);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return facilityArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return facilityArrayList;
    }

    public ArrayList<MaritalStatus> getSubCenterData(String talukaId) {

        readDatabase();
        ArrayList<MaritalStatus> facilityArrayList = new ArrayList<>();
        try {
            String Query = "select ss.subcentreId,ss.subcentre\n" +
                    "from tbl_subcentre as ss\n" +
                    "inner join tbl_phc as phc on phc.phcId=ss.phcId\n" +
                    "inner join tbl_taluka as tt on tt.talukaId=phc.talukaId\n" +
                    "where phc.talukaId='" + talukaId + "'";
            Cursor cursor = mDataBase.rawQuery(Query, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.select_subcenter));
            facilityArrayList.add(state);

            if (cursor.moveToFirst()) {

                do {
                    MaritalStatus facility = new MaritalStatus();
                    facility.setId(cursor.getString(cursor.getColumnIndex("subcentreId")));
                    facility.setStatus(cursor.getString(cursor.getColumnIndex("subcentre")));
                    facilityArrayList.add(facility);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return facilityArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return facilityArrayList;
    }

    public ArrayList<MaritalStatus> getPhcData(String talukaId) {

        readDatabase();
        ArrayList<MaritalStatus> facilityArrayList = new ArrayList<>();
        try {
            String Query = "select phcId,phc\n" +
                    "from tbl_phc\n" +
                    "where talukaId='" + talukaId + "'";
            Cursor cursor = mDataBase.rawQuery(Query, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.select_phc));
            facilityArrayList.add(state);

            if (cursor.moveToFirst()) {

                do {
                    MaritalStatus facility = new MaritalStatus();
                    facility.setId(cursor.getString(cursor.getColumnIndex("phcId")));
                    facility.setStatus(cursor.getString(cursor.getColumnIndex("phc")));
                    facilityArrayList.add(facility);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return facilityArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return facilityArrayList;
    }

    public ArrayList<MaritalStatus> getDesignation(String facilityId, String centerId) {

        readDatabase();
        ArrayList<MaritalStatus> facilityArrayList = new ArrayList<>();
        try {
            String Query = "select e.employeeId,d.designation,d.designationId,e.firstName,e.middleName,e.lastName\n" +
                    "from tbl_employeeprofile as e\n" +
                    "inner join tbl_designation as d on e.designationId=d.designationId\n" +
                    "where e.facilityId='" + facilityId + "' and e.phcId='" + centerId + "'";

            Cursor cursor = mDataBase.rawQuery(Query, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.delivery_designation));
            facilityArrayList.add(state);

            if (cursor.moveToFirst()) {

                do {
                    MaritalStatus facility = new MaritalStatus();
                    facility.setId(cursor.getString(cursor.getColumnIndex("employeeId")) + "," + cursor.getString(cursor.getColumnIndex("designation")));
                    facility.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    facilityArrayList.add(facility);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return facilityArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return facilityArrayList;
    }

    public ArrayList<MaritalStatus> getDesignationBySubcenter(String talukaId, String centerId) {

        readDatabase();
        ArrayList<MaritalStatus> facilityArrayList = new ArrayList<>();
        try {
            String Query = "select e.employeeId,e.designationId,d.designation,e.firstName,e.middleName,e.lastName\n" +
                    "from tbl_subcentre as ss\n" +
                    "left join tbl_phc as phc on phc.phcId=ss.phcId\n" +
                    "left join tbl_taluka as tt on tt.talukaId=phc.talukaId\n" +
                    "left join tbl_employeeprofile as e on e.subcentreId=ss.subcentreId\n" +
                    "left join tbl_designation as d on d.designationId=e.designationId\n" +
                    "where phc.talukaId='" + talukaId + "' and e.subcentreId='" + centerId + "'";

            Cursor cursor = mDataBase.rawQuery(Query, null);

            MaritalStatus state = new MaritalStatus();
            state.setId("0000");
            state.setStatus(mContext.getResources().getString(R.string.delivery_designation));
            facilityArrayList.add(state);

            if (cursor.moveToFirst()) {

                do {
                    MaritalStatus facility = new MaritalStatus();
                    facility.setId(cursor.getString(cursor.getColumnIndex("employeeId")) + "," + cursor.getString(cursor.getColumnIndex("designation")));
                    facility.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    facilityArrayList.add(facility);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return facilityArrayList;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return facilityArrayList;
    }

    public boolean setMTPDate(String pregnantwomanregdId, String pregnancyoutcomeDate) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("pregnancyoutcomeDate", pregnancyoutcomeDate);
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.TABLE_PREGNANT_WOMENREG, contentValues, " pregnantwomanregdId='" + pregnantwomanregdId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean insertPregnancyOutCome(DeliveryDetails deliveryDetails) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_PREGNANCYOUTCOMEREGD + "(" +
                    "pregnantwomanregdId,pregnancyoutcomeregdDate,pregancyoutcometypeId,employeeId,facilityId," +
                    "facilitynameId,pregancyoutcomeDate,pregnancyoutcomeTime,dischargeDate,dischargeTime,numberofChild," +
                    "deliveryIncentive,deliverymamtaKit,delivery108Service,createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
//
            insert.bindString(1, deliveryDetails.getPregnantwomanregdId());
            insert.bindString(2, deliveryDetails.getPregnancyoutcomeregdDate());
            insert.bindString(3, deliveryDetails.getPregancyoutcometypeId());
            insert.bindString(4, deliveryDetails.getEmployeeId());
            insert.bindString(5, deliveryDetails.getFacilityId());
            insert.bindString(6, deliveryDetails.getFacilitynameId());
            insert.bindString(7, deliveryDetails.getPregancyoutcomeDate());
            insert.bindString(8, deliveryDetails.getPregnancyoutcomeTime());
            insert.bindString(9, deliveryDetails.getDischargeDate());
            insert.bindString(10, deliveryDetails.getDischargeTime());
            insert.bindString(11, deliveryDetails.getNumberofChild());
            insert.bindString(12, deliveryDetails.getDeliveryIncentive());
            insert.bindString(13, deliveryDetails.getDeliverymamtaKit());
            insert.bindString(14, deliveryDetails.getDelivery108Service());
            insert.bindString(15, dateFormat.format(date));
            insert.bindString(16, deliveryDetails.getUserId());
            insert.bindString(17, "1");
            insert.execute();

            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertTreatment(DeliveryDetails deliveryDetails) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_PREGNANCYOUTCOMETREATMENT + "(" +
                    "pregnantwomanregdId,pregancyoutcometreatmentId,pregancyoutcomeDate,createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (deliveryDetails.getPregnancyoutcomeTreatment() != null) {
                for (int i = 0; i < deliveryDetails.getPregnancyoutcomeTreatment().length; i++) {
                    insert.bindString(1, deliveryDetails.getPregnantwomanregdId());
                    insert.bindString(2, deliveryDetails.getPregnancyoutcomeTreatment()[i]);
                    insert.bindString(3, deliveryDetails.getPregancyoutcomeDate());
                    insert.bindString(4, dateFormat.format(date));
                    insert.bindString(5, deliveryDetails.getUserId());
                    insert.bindString(6, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertPostnatalService(DeliveryDetails deliveryDetails) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_POSTNATALSERVICES + "(" +
                    "pregnantwomanregdId,postnatalservicetypeId,postnatalservicedueDate,createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            int j = 0;
            if (deliveryDetails.getPostnatalservicedueDate() != null) {
                for (int i = 0; i < deliveryDetails.getPostnatalservicedueDate().length; i++) {
                    insert.bindString(1, deliveryDetails.getPregnantwomanregdId());
                    j = j + 1;
                    insert.bindString(2, "" + j);
                    insert.bindString(3, deliveryDetails.getPostnatalservicedueDate()[i]);
                    insert.bindString(4, dateFormat.format(date));
                    insert.bindString(5, deliveryDetails.getUserId());
                    insert.bindString(6, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean insertNewChild(DeliveryDetails deliveryDetails) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_CHILDREG + "(" +
                    "childregdId,pregnantwomanregdId,pregancyoutcomeDate,childregdDate,createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            int j = 0;
            if (deliveryDetails.getNumberofChild() != null) {
                for (int i = 0; i < Integer.parseInt(deliveryDetails.getNumberofChild()); i++) {
                    Random emamtaRandom = new Random();
                    int chId = emamtaRandom.nextInt(900000) + 100000;

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);

                    String strChildId = "CH/" + year + "/" + chId;

                    insert.bindString(1, strChildId);
                    insert.bindString(2, deliveryDetails.getPregnantwomanregdId());
                    insert.bindString(3, deliveryDetails.getPregancyoutcomeDate());
                    insert.bindString(4, deliveryDetails.getPregnancyoutcomeregdDate());
                    insert.bindString(5, dateFormat.format(date));
                    insert.bindString(6, deliveryDetails.getUserId());
                    insert.bindString(7, "1");
                    insert.execute();
                }
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public ArrayList<HashMap<String, String>> getChilds(String id, String strVillageId, String strVillageName) {
        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
        String selectQuery;
        readDatabase();
        try {
            if (id == null) {
                selectQuery = "select cr.childregdId,cr.pregnantwomanregdId,cr.pregancyoutcomeDate,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,m.emamtafamilyId\n" +
                        "from tbl_childregistration as cr\n" +
                        "inner join tbl_pregnantwomanregd as pw on cr.pregnantwomanregdId=pw.pregnantwomanregdId\n" +
                        "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                        "where cr.emamtahealthId is null and cr.birthStatus is null and cr.birthWeight is null and m.villageId='" + strVillageId + "'";
            } else {
                selectQuery = "select cr.childregdId,cr.pregnantwomanregdId,cr.pregancyoutcomeDate,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,m.emamtafamilyId\n" +
                        "from tbl_childregistration as cr\n" +
                        "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                        "inner join tbl_pregnantwomanregd as pw on cr.pregnantwomanregdId=pw.pregnantwomanregdId\n" +
                        "where cr.pregnantwomanregdId='" + id + "' and cr.emamtahealthId is null and cr.birthStatus is null and cr.birthWeight is null and m.villageId='" + strVillageId + "'";
            }
            Log.e(" Query", "getChilds = " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("childregdId", cursor.getString(cursor.getColumnIndex("childregdId")));
                    hashMap.put("pregnantwomanregdId", cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    hashMap.put("pregancyoutcomeDate", cursor.getString(cursor.getColumnIndex("pregancyoutcomeDate")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    hashMap.put("villageId", strVillageId);
                    hashMap.put("villageName", strVillageName);
                    childArrayList.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return childArrayList;
    }

    public boolean saveChildData(HashMap<String, String> hashMap) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("birthStatus", hashMap.get("birthStatus"));
            contentValues.put("birthWeight", hashMap.get("birthWeight"));
            contentValues.put("breastFeeding", hashMap.get("breastFeeding"));
            contentValues.put("kangarooCare", hashMap.get("kangarooCare"));
            contentValues.put("birthComplication", hashMap.get("birthComplication"));
            contentValues.put("emamtahealthId", hashMap.get("emamtahealthId"));
            contentValues.put("corticosteroid", hashMap.get("corticosteroid"));
            contentValues.put("injvitaminK", hashMap.get("injvitaminK"));
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.TABLE_CHILDREG, contentValues, " childregdId='" + hashMap.get("childregdId") + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean saveNewChildMember(HashMap<String, String> hashMap) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(emamtahealthId,emamtafamilyId,villageId,firstName,middleName," +
                    "lastName,isHead,gender,birthDate,createdbyuserId,createdDate,isActive,isUpdated" +
                    " ) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            insert.bindString(1, hashMap.get("emamtahealthId"));
            insert.bindString(2, hashMap.get("emamtafamilyId"));
            insert.bindString(3, hashMap.get("villageId"));
            insert.bindString(4, hashMap.get("firstName"));
            insert.bindString(5, hashMap.get("middleName"));
            insert.bindString(6, hashMap.get("lastName"));
            insert.bindString(7, hashMap.get("isHead"));
            insert.bindString(8, hashMap.get("gender"));
            insert.bindString(9, hashMap.get("birthDate"));
            insert.bindString(10, hashMap.get("userId"));
            insert.bindString(11, dateFormat.format(date));
            insert.bindString(12, "1");
            insert.bindString(13, "1");

            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean setChildVaccination(ArrayList<HashMap<String, String>> hashMap) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_CHILDIMMUNIZATION + "(childregdId,emamtahealthId,vaccineName,vaccinedueDate," +
                    "createdbyuserId,createdDate,isUpdated" +
                    " ) values (?,?,?,?,?,?,?)";

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            for (int i = 0; i < hashMap.size(); i++) {
                HashMap<String, String> map = hashMap.get(i);
                insert.bindString(1, map.get("childregdId"));
                insert.bindString(2, map.get("emamtahealthId"));
                insert.bindString(3, map.get("vaccineName"));
                insert.bindString(4, map.get("vaccinedueDate"));
                insert.bindString(5, map.get("userId"));
                insert.bindString(6, dateFormat.format(date));
                insert.bindString(7, "1");
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public ArrayList<HashMap<String, String>> getPostNatalServiceData(String strVillageId, String strVillageName) {
        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
        String selectQuery;
        readDatabase();
        try {
            selectQuery = "select pw.pregnantwomanregdId,pregnancyoutcomeDate,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,m.emamtafamilyId\n" +
                    "from tbl_pregnantwomanregd as pw\n" +
                    "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                    "where pregnancyoutcomeDate is not null and m.villageId='" + strVillageId + "'";

            Log.e(" Query", "getPostNatalServiceData = " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pregnantwomanregdId", cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    hashMap.put("pregnancyoutcomeDate", cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    hashMap.put("villageId", strVillageId);
                    hashMap.put("villageName", strVillageName);
                    if (cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")) != null) {
                        if (!cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")).equals("null")) {
                            String lstDate = Utils.addDays(cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")), 50);
                            SimpleDateFormat simpleDateFormat =
                                    new SimpleDateFormat("dd/M/yyyy");
                            Date pregnancyoutcomeDate = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")));
                            Date lastDate = simpleDateFormat.parse(lstDate);
                            boolean isTrue = Utils.checkBeforeDate(pregnancyoutcomeDate, lastDate);
                            if (isTrue) {
                                childArrayList.add(hashMap);
                            }
                        }
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return childArrayList;
    }

    public ArrayList<HashMap<String, String>> getPostNatalServiceListData(String pregnancyoutcomeregdId, String strVillageId, String strVillageName) {
        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
        String selectQuery;
        readDatabase();
        try {
            selectQuery = "select pn.postnatalserviceId,pn.postnatalservicetypeId,pn.pregnantwomanregdId,pw.pregnancyoutcomeDate,pn.postnatalservicedueDate,pn.postnatalservicegivenDate,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,m.emamtafamilyId\n" +
                    "from tbl_postnatalService as pn\n" +
                    "inner join tbl_pregnantwomanregd as pw on pw.pregnantwomanregdId=pn.pregnantwomanregdId\n" +
                    "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                    "where m.villageId='" + strVillageId + "' and pn.pregnantwomanregdId='" + pregnancyoutcomeregdId + "'";

            Log.e(" Query", "getPostNatalServiceListData = " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("postnatalserviceId", cursor.getString(cursor.getColumnIndex("postnatalserviceId")));
                    hashMap.put("postnatalservicetypeId", cursor.getString(cursor.getColumnIndex("postnatalservicetypeId")));
                    hashMap.put("pregnantwomanregdId", cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    hashMap.put("pregnancyoutcomeDate", cursor.getString(cursor.getColumnIndex("pregnancyoutcomeDate")));
                    hashMap.put("postnatalservicedueDate", cursor.getString(cursor.getColumnIndex("postnatalservicedueDate")));
                    hashMap.put("postnatalservicegivenDate", cursor.getString(cursor.getColumnIndex("postnatalservicegivenDate")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    hashMap.put("villageId", strVillageId);
                    hashMap.put("villageName", strVillageName);
                    childArrayList.add(hashMap);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return childArrayList;
    }

    public boolean savePostNatalData(HashMap<String, String> hashMap, String postnatalserviceId) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("postnatalservicegivenDate", hashMap.get("postnatalservicegivenDate"));
            contentValues.put("plannedfpMethod", hashMap.get("plannedfpMethod"));
            contentValues.put("postnatalComplication", hashMap.get("postnatalComplication"));
            contentValues.put("isReferred", hashMap.get("isReferred"));
            contentValues.put("memberStatus", hashMap.get("memberStatus"));
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.TABLE_POSTNATALSERVICES, contentValues, " postnatalserviceId='" + postnatalserviceId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public ArrayList<HashMap<String, String>> searchMemberForFP(String searchString, String strVillageId) {

        ArrayList<HashMap<String, String>> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo,m.photovalue,m.birthDate\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.firstName LIKE '%" + searchString + "%' and m.isActive=1 and fm.villageId='" + strVillageId + "'";
            /*String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.villageId='" + strVillageId + "'";*/
            Log.e("searchFamilyMember", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> family = new HashMap<>();
                    family.put("memberId", cursor.getString(cursor.getColumnIndex("memberId")));
                    family.put("familyId", cursor.getString(cursor.getColumnIndex("familyId")));
                    family.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    family.put("name", cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
//                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        family.put("photo", cursor.getString(cursor.getColumnIndex("photo")));
                        family.put("photovalue", cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    family.put("birthDate", cursor.getString(cursor.getColumnIndex("birthDate")));

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MMM-yyyy");
                    Date currentDate = new Date();
                    Date date = dateFormat.parse(new SimpleDateFormat("dd-MMM-yyyy").format(currentDate));
                    Date birthDate = dateFormat.parse(family.get("birthDate"));
                    int year = Utils.getYear(birthDate, date);

                    if (year >= 15 && year <= 49) {
                        family.put("age", "" + year);
                        familyArrayList.add(family);
                    }


                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public boolean saveFPMethod(HashMap<String, String> hashMap) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.TABLE_ADOPTEDMETHOD + "(emamtahealthId,emamtafamilyId,villageId,employeeId,facilityId," +
                    "adoptedfpMethod,createdbyuserId,createdDate,isUpdated,copperttypeId,adoptedDate" +
                    " ) values (?,?,?,?,?,?,?,?,?,?,?)";

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            insert.bindString(1, hashMap.get("emamtahealthId"));
            insert.bindString(2, hashMap.get("emamtafamilyId"));
            insert.bindString(3, hashMap.get("villageId"));
            insert.bindString(4, hashMap.get("employeeId"));
            insert.bindString(5, hashMap.get("facilityId"));
            insert.bindString(6, hashMap.get("adoptedfpMethod"));
            insert.bindString(7, hashMap.get("createdbyuserId"));
            insert.bindString(8, dateFormat.format(date));
            insert.bindString(9, "1");
            if (hashMap.get("copperttypeId") != null) {
                insert.bindString(10, hashMap.get("copperttypeId"));
            }
            insert.bindString(11, hashMap.get("adoptedDate"));
            insert.execute();
            mDataBase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (mDataBase != null) {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public ArrayList<HashMap<String, String>> searchFamilyPlanningMethod(String emamtahealthId) {
        ArrayList<HashMap<String, String>> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select afpm.adoptedfpmethodId,afpm.adoptedDate,fpm.familyplanningMethod,m.emamtahealthId,afpm.adoptedfpMethod,m.firstName,m.middleName,m.lastName,afpm.discontinueDate\n" +
                    "from tbl_adoptedfpmethod as afpm\n" +
                    "inner join tbl_familyplanningmethod as fpm on fpm.familyplanningmethodId=afpm.adoptedfpMethod\n" +
                    "inner join tbl_member as m on m.emamtahealthId=afpm.emamtahealthId\n" +
                    "where afpm.emamtahealthId='" + emamtahealthId + "'\n" +
                    "ORDER BY afpm.adoptedDate Asc";

            Log.e("selectQuery", "searchFamilyPlanningMethod => " + selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> family = new HashMap<>();
                    family.put("adoptedfpmethodId", cursor.getString(cursor.getColumnIndex("adoptedfpmethodId")));
                    family.put("adoptedDate", cursor.getString(cursor.getColumnIndex("adoptedDate")));
                    family.put("familyplanningMethod", cursor.getString(cursor.getColumnIndex("familyplanningMethod")));
                    family.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    family.put("adoptedfpMethod", cursor.getString(cursor.getColumnIndex("adoptedfpMethod")));
                    family.put("name", cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    family.put("discontinueDate", cursor.getString(cursor.getColumnIndex("discontinueDate")));
                    familyArrayList.add(family);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public boolean saveUpdatedFpMethod(String adoptedfpmethodId, HashMap<String, String> hashMap) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("discontinueDate", hashMap.get("discontinueDate"));
            contentValues.put("reasonforremovalId", hashMap.get("reasonforremovalId"));
            contentValues.put("fpComplication", hashMap.get("fpComplication"));
            contentValues.put("fpcomplicationDate", hashMap.get("fpcomplicationDate"));
            contentValues.put("statusafterComplication", hashMap.get("statusafterComplication"));
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.TABLE_ADOPTEDMETHOD, contentValues, " adoptedfpmethodId='" + adoptedfpmethodId + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public boolean updateUserProfile(HashMap<String, String> hashMap) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("photovalue", hashMap.get("photovalue"));
            contentValues.put("photo", hashMap.get("photo"));
            contentValues.put("firstName", hashMap.get("firstName"));
            contentValues.put("middleName", hashMap.get("middleName"));
            contentValues.put("lastName", hashMap.get("lastName"));
            contentValues.put("updatedDate", dateFormat.format(date));
            contentValues.put("isUpdated", "2");
            mDataBase.update(DBConstant.TABLE_EMPLOYEE_PROFILE, contentValues, " userId='" + hashMap.get("userId") + "'", null);
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
            return false;
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return true;
    }

    public HashMap<String, String> getUserProfile(String userId) {
        HashMap<String, String> userProfile = new HashMap<>();
        readDatabase();
        try {
            String selectQuery = "select ep.firstName,ep.middleName,ep.lastName,ep.photo,ep.photovalue,ep.mobileNumber,sc.subcentre\n" +
                    "from tbl_employeeprofile as ep\n" +
                    "inner join tbl_subcentre as sc on sc.subcentreId=ep.subcentreId\n" +
                    "where userId='" + userId + "'";

            Log.e("selectQuery", "searchFamilyPlanningMethod => " + selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex("photo")) != null) {
                        userProfile.put("photo", cursor.getString(cursor.getColumnIndex("photo")));
                        userProfile.put("photovalue", cursor.getString(cursor.getColumnIndex("photovalue")));
                    }
                    userProfile.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    userProfile.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    userProfile.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    userProfile.put("mobileNumber", cursor.getString(cursor.getColumnIndex("mobileNumber")));
                    userProfile.put("subcentre", cursor.getString(cursor.getColumnIndex("subcentre")));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return userProfile;
    }

    public ArrayList<HashMap<String, String>> getAncWorkPlan(String strVillageId, String date) {
        ArrayList<HashMap<String, String>> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select anc.pregnantwomanregdId,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,anc.ancservicedueDate,anc.ancservicetrimesterId\n" +
                    "from tbl_ancservice as anc\n" +
                    "inner join tbl_pregnantwomanregd as pw on pw.pregnantwomanregdId=anc.pregnantwomanregdId\n" +
                    "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                    "where m.villageId='" + strVillageId + "'";

            Log.e("selectQuery", "getAncWorkPlan => " + selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pregnantwomanregdId", cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("ancservicedueDate", cursor.getString(cursor.getColumnIndex("ancservicedueDate")));
                    hashMap.put("ancservicetrimesterId", cursor.getString(cursor.getColumnIndex("ancservicetrimesterId")));

                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("dd/M/yyyy");
                    Date ancservicedueDate = simpleDateFormat.parse(hashMap.get("ancservicedueDate"));
                    Date lastDate = simpleDateFormat.parse(date);
                    boolean isTrue = Utils.checkBeforeDate(ancservicedueDate, lastDate);
                    if (isTrue) {
                        familyArrayList.add(hashMap);
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public ArrayList<HashMap<String, String>> getEstimatePregnancy(String strVillageId) {
        ArrayList<HashMap<String, String>> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select pw.pregnantwomanregdId,pw.emamtahealthId,m.firstName,m.middleName,m.lastName,pw.eddDate\n" +
                    "from tbl_pregnantwomanregd as pw\n" +
                    "inner join tbl_member as m on m.emamtahealthId=pw.emamtahealthId\n" +
                    "where m.villageId='" + strVillageId + "'";

            Log.e(TAG, "getEstimatePregnancy => " + selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pregnantwomanregdId", cursor.getString(cursor.getColumnIndex("pregnantwomanregdId")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("eddDate", cursor.getString(cursor.getColumnIndex("eddDate")));
                    familyArrayList.add(hashMap);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public ArrayList<HashMap<String, String>> getChildImmunizeData(String strVillageId,String strVillageName) {
        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
        String selectQuery;
        readDatabase();
        try {
            selectQuery = "select distinct cim.childregdId,cim.emamtahealthId,m.firstName,m.middleName,m.lastName,m.birthDate\n" +
                    "from tbl_member as m\n" +
                    "Inner join tbl_childimmunization as cim on cim.emamtahealthId=m.emamtahealthId\n" +
                    "where cim.vaccinegivenDate is null or cim.hepatitis0givenDate is null or cim.polio0givenDate is null or \n" +
                    "cim.polio1givenDate is null or cim.pentavalent1givenDate is null or cim.polio2givenDate is null or cim.pentavalent2givenDate is null or \n" +
                    "cim.polio3givenDate is null or cim.pentavalent3givenDate is null or cim.measles1givenDate is null or cim.vitamina1givenDate is null\n" +
                    "and m.villageId='"+strVillageId+"'";

            Log.e(TAG, "getChildImmunizeData => " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("childregdId", cursor.getString(cursor.getColumnIndex("childregdId")));
                    hashMap.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    hashMap.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    hashMap.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    hashMap.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    hashMap.put("birthDate", cursor.getString(cursor.getColumnIndex("birthDate")));
                    hashMap.put("villageId", strVillageId);
                    hashMap.put("villageName", strVillageName);
                    childArrayList.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return childArrayList;
    }

    public ArrayList<HashMap<String, String>> getChildImmunizeDetails(String childregdId) {
        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
        String selectQuery;
        readDatabase();
        try {
            selectQuery = "select vaccineName,vaccinedueDate,vaccinegivenDate\n" +
                    "from  tbl_childimmunization \n" +
                    "where childregdId='"+childregdId+"'";

            Log.e(TAG, "getChildImmunizeDetails => " + selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("vaccineName", cursor.getString(cursor.getColumnIndex("vaccineName")));
                    hashMap.put("vaccinedueDate", cursor.getString(cursor.getColumnIndex("vaccinedueDate")));
                    hashMap.put("vaccinegivenDate", cursor.getString(cursor.getColumnIndex("vaccinegivenDate")));
                    childArrayList.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return childArrayList;
    }
}
