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
import java.util.Date;
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
                    "macardNumber,lattitudes,longitude,createdbyuserId,createdDate,isActive,anganwadiId,migrationtypeId" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(familyId,emamtafamilyId,villageId,photo," +
                    "firstName,middleName,lastName,isHead,gender,maritalStatus,birthDate,mobileNo,createdbyuserId," +
                    "createdDate,isActive,emamtahealthId" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, lastMember.getFamilyId());
            insert.bindString(2, lastMember.getEmamtafamilyId());
            insert.bindString(3, member.getVillageId());
            if (member.getPhoto() != null) {
                insert.bindString(4, member.getPhoto());
            }
            insert.bindString(5, member.getFirstName());
            insert.bindString(6, member.getMiddleName());
            insert.bindString(7, member.getLastName());
            insert.bindString(8, "1");
            insert.bindString(9, member.getGender());
            insert.bindString(10, member.getMaritalStatus());
            insert.bindString(11, member.getBirthDate());
            insert.bindString(12, member.getMobileNo());
            insert.bindString(13, member.getUserId());
            insert.bindString(14, dateFormat.format(date));
            insert.bindString(15, "1");
            insert.bindString(16, member.getEmamtahealthId());
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

    public ArrayList<Family> searchFamily(String number, String villageId) {
//        number="FM/2009/7339895";
        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.migrationtypeId!='0' and fm.isActive=1 and fm.villageId='" + villageId + "'";
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

    public boolean insertMemberData(JSONArray jsonMemberArray) {

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(memberId,emamtahealthId,familyId,emamtafamilyId,villageId," +
                    "emrNumber,photo,firstName,middleName,lastName,firstnameEng,middlenameEng,lastnameEng,isHead,relationwithheadId," +
                    "gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod,wanttoadoptfpMethod,plannedfpMethod,isPregnant" +
                    ",wantChild,memberStatus,menstruationStatus,adharcardNumber,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber" +
                    ",migratedemamtafamilyId,migratedemamtamemberId,createdbyuserId,createdDate,updatedDate,subcentreId,isActive," +
                    "migratedvillagId,migrationtypeId,isUpdated,photovalue" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                if (jsonObject.getString("photovalue") != null) {
                    byte[] decodedString = Base64.decode(jsonObject.getString("photovalue"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    String filePath = Utils.saveImageWithName(decodedByte, jsonObject.getString("photo"), Constants.APP_NAME, Constants.PROFILE_PIC);
                    insert.bindString(44, filePath);
                }

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
                    "updatedDate) " +
                    "values (?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, villageId);
            insert.bindString(2, faliyaName);
            insert.bindString(3, isRisky);
            insert.bindString(4, userId);
            insert.bindString(5, dateFormat.format(date));
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
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.emamtahealthId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.migrationtypeId!='0' and m.isActive=1 and fm.villageId='" + strVillageId + "'";
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
                    "WHERE fm.migrationtypeId!='0' and m.isActive=1 and fm.villageId='" + strVillageId + "' LIMIT 10";
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
                    "m.adharcardNumber,m.electioncardNumber,m.pancardNumber,m.drivingcardNumer,m.passportcardNumber,m.relationwithheadId,m.photovalue " +
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

    public boolean insertNewMember(Member familyMember) {
        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(emamtahealthId,emamtafamilyId,villageId,firstName,middleName," +
                    "lastName,isHead,relationwithheadId,gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod," +
                    "wanttoadoptfpMethod,plannedfpMethod,isPregnant,wantChild,memberStatus,menstruationStatus,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber,createdbyuserId,createdDate,photo,isActive" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
            insert.bindString(29, "1");
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

    public ArrayList<MaritalStatus> getDistrictData() {

        ArrayList<MaritalStatus> arrayListDistrict = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "select districtId,district from tbl_district where stateId=24";

            Log.v("getDistrictData Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
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
            String selectQuery = "select talukaId,taluka from tbl_taluka where districtId='" + dirstID + "'";

            Log.v("getTaluka Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
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
            String selectQuery = "select villageId,village from tbl_village where talukaId='" + talukaID + "'";

            Log.v("getTaluka Query", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus member = new MaritalStatus();
                    member.setId(cursor.getString(cursor.getColumnIndex("villageId")));
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

    public boolean migrateFamily(String eMamtaId, String villageID, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedvillagId", villageID);
            contentValues.put("migrationtypeId", isParmenant);
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

    public boolean migrateFamilyMember(String eMamtaId, String eHealthId, String villageID, String isParmenant) {
        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("migratedemamtafamilyId", eMamtaId);
            contentValues.put("migratedemamtamemberId", eHealthId);
            contentValues.put("migratedvillagId", villageID);
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
            String sql = "Insert into " + DBConstant.AGANWADI_TABLE + "(anganwadiId,talukaId,subcentreId,villageId,anganwadi,createdDate,updatedDate,createdbyuserId) " +
                    "values (?,?,?,?,?,?,?,?)";
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
                    "districtId,talukaId,phcId,subcenterId,villageId,firstName,middleName,lastnName,photo,gender,mobileNumber," +
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
                insert.bindString(12, jsonObject.getString("subcenterId"));
                insert.bindString(13, jsonObject.getString("villageId"));
                insert.bindString(14, jsonObject.getString("firstName"));
                insert.bindString(15, jsonObject.getString("middleName"));
                insert.bindString(16, jsonObject.getString("lastnName"));
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
                    "where isUpdated='2'";

            Log.v("familyMamberData", selectQuery);
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject member = new JSONObject();
                    member.put("memberId", cursor.getString(cursor.getColumnIndex("memberId")));
                    member.put("photo", cursor.getString(cursor.getColumnIndex("photo")));
                    member.put("emamtahealthId", cursor.getString(cursor.getColumnIndex("emamtahealthId")));
                    member.put("familyId", cursor.getString(cursor.getColumnIndex("familyId")));
                    member.put("emamtafamilyId", cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    member.put("villageId", cursor.getString(cursor.getColumnIndex("villageId")));
                    if (cursor.getString(cursor.getColumnIndex("employeeId")) != null) {
                        member.put("employeeId", cursor.getString(cursor.getColumnIndex("employeeId")));
                    } else {
                        member.put("employeeId", "null");
                    }
                    member.put("emrNumber", cursor.getString(cursor.getColumnIndex("emrNumber")));
                    member.put("firstName", cursor.getString(cursor.getColumnIndex("firstName")));
                    member.put("middleName", cursor.getString(cursor.getColumnIndex("middleName")));
                    member.put("lastName", cursor.getString(cursor.getColumnIndex("lastName")));
                    member.put("firstnameEng", cursor.getString(cursor.getColumnIndex("firstnameEng")));
                    member.put("middlenameEng", cursor.getString(cursor.getColumnIndex("middlenameEng")));
                    member.put("lastnameEng", cursor.getString(cursor.getColumnIndex("lastnameEng")));
                    member.put("isHead", cursor.getString(cursor.getColumnIndex("isHead")));
                    member.put("relationwithheadId", cursor.getString(cursor.getColumnIndex("relationwithheadId")));
                    member.put("gender", cursor.getString(cursor.getColumnIndex("gender")));
                    member.put("maritalStatus", cursor.getString(cursor.getColumnIndex("maritalStatus")));
                    member.put("birthDate", cursor.getString(cursor.getColumnIndex("birthDate")));
                    member.put("mobileNo", cursor.getString(cursor.getColumnIndex("mobileNo")));
                    member.put("childof", cursor.getString(cursor.getColumnIndex("childof")));
                    member.put("wifeof", cursor.getString(cursor.getColumnIndex("wifeof")));
                    member.put("adoptedfpMethod", cursor.getString(cursor.getColumnIndex("adoptedfpMethod")));
                    member.put("wanttoadoptfpMethod", cursor.getString(cursor.getColumnIndex("wanttoadoptfpMethod")));
                    member.put("plannedfpMethod", cursor.getString(cursor.getColumnIndex("plannedfpMethod")));
                    member.put("isPregnant", cursor.getString(cursor.getColumnIndex("isPregnant")));
                    member.put("wantChild", cursor.getString(cursor.getColumnIndex("wantChild")));
                    member.put("memberStatus", cursor.getString(cursor.getColumnIndex("memberStatus")));
                    member.put("menstruationStatus", cursor.getString(cursor.getColumnIndex("menstruationStatus")));
                    member.put("adharcardNumber", cursor.getString(cursor.getColumnIndex("adharcardNumber")));
                    member.put("electioncardNumber", cursor.getString(cursor.getColumnIndex("electioncardNumber")));
                    member.put("pancardNumber", cursor.getString(cursor.getColumnIndex("pancardNumber")));
                    member.put("drivingcardNumer", cursor.getString(cursor.getColumnIndex("drivingcardNumer")));
                    member.put("passportcardNumber", cursor.getString(cursor.getColumnIndex("passportcardNumber")));
                    member.put("migratedemamtafamilyId", cursor.getString(cursor.getColumnIndex("migratedemamtafamilyId")));
                    member.put("migratedemamtamemberId", cursor.getString(cursor.getColumnIndex("migratedemamtamemberId")));
                    member.put("createdbyuserId", cursor.getString(cursor.getColumnIndex("createdbyuserId")));
                    member.put("createdDate", cursor.getString(cursor.getColumnIndex("createdDate")));
                    member.put("updatedDate", cursor.getString(cursor.getColumnIndex("updatedDate")));
                    member.put("isActive", cursor.getString(cursor.getColumnIndex("isActive")));
                    member.put("subcentreId", cursor.getString(cursor.getColumnIndex("subcentreId")));
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

    public ArrayList<Member> searchFemales(String searchString, String villageId) {
        ArrayList<Member> womenMemberList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "Select memberId,emamtafamilyId,emamtahealthId,firstName,middleName,lastName,birthDate,photo,photovalue " +
                    "from tbl_member \n" +
                    "where gender='F' and maritalStatus!='2' and villageId='" + villageId + "' and firstName LIKE '" + searchString + "%'";

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
            String selectQuery = "Select m.emamtahealthId,m.memberId,fm.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.birthDate,m.mobileNo,m.photo,m.photovalue,fm.isBpl,fm.racialId\n" +
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
                    "emamtahealthId,ishighRisk,isJsy,isKpsy,isCy,employeeId,createdDate,createdbyuserId,isUpdated) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            if (pregnantWomen.getEmamtaId() != null) {
                insert.bindString(17, pregnantWomen.getEmployeeId());
            }
            insert.bindString(18, dateFormat.format(date));
            insert.bindString(19, pregnantWomen.getUserId());
            insert.bindString(20, "1");
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
                    "highriskmothersymptomId,pregnantwomanregdId,ancserviceDate,createdDate,createdbyuserId,ancservicetypeId) " +
                    "values (?,?,?,?,?,?)";
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
                    "pregnantwomanregdId,ancservicedueDate,createddate) " +
                    "values (?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            if (pregnantWomen.getAncServices() != null) {
                int j = 1;
                for (int i = 0; i < pregnantWomen.getAncServices().length; i++) {
                    insert.bindString(1, pregnantWomen.getPregnantwomanregdId());
                    insert.bindString(2, pregnantWomen.getAncServices()[i]);
//                    insert.bindLong(3, j++);
                    insert.bindString(3, dateFormat.format(date));
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
            String selectQuery = "select employeeId,firstName,middleName,lastnName\n" +
                    "from tbl_employeeprofile\n" +
                    "where facilityId='101' and designationId='25' and villageId='" + villageId + "'";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("employeeId")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastnName")));
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
}
