package com.mcts.app.db;

import java.io.File;
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
import android.util.Log;

import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.model.Member;
import com.mcts.app.model.Religion;

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

    public int getdatabaselenght() {
        int db_length = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from CheckIN_master", null);
        if (c != null) {
            while (c.moveToNext()) {
                db_length++;
            }
        }
        return db_length;
    }

    public ArrayList<Religion> getReligionData() {

        ArrayList<Religion> religionArrayList = new ArrayList<>();
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


        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return religionArrayList;
    }

/* Family*/
    public boolean saveFamilyDetails(String familyHeadName, String husbandName, String sirName, String familyNumber, String houseNumber, String address, String strCast, String strReligion, String isAns, String lat, String lng) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("family_number", familyNumber);
        contentValues.put("religion", strReligion);
        contentValues.put("racial", strCast);
        contentValues.put("is_bpl", isAns);
        contentValues.put("created", dateFormat.format(date));
        contentValues.put("location_id", 123);
        contentValues.put("sc_id", 1);
        contentValues.put("migrated_location_id", 1);
        contentValues.put("added_by_user_id", 123);
        contentValues.put("home_number", houseNumber);
        contentValues.put("address", address);
        contentValues.put("address1", "address");
        contentValues.put("migration_type_id", 123);
        contentValues.put("lattitude", lat);
        contentValues.put("longitude", lng);
        contentValues.put("family_head_name", familyHeadName);
        contentValues.put("head_father_name", husbandName);
        contentValues.put("head_sir_name", sirName);
        db.insert("tbl_family", null, contentValues);
        return true;
    }

    public boolean updateFamilyDetails(Member member){

        writeDatabase();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            ContentValues contentValues = new ContentValues();
            contentValues.put("houseNumber",member.getHouseNumber());
            contentValues.put("landmark",member.getLandmark());
            contentValues.put("landmark",member.getLandmark());
            contentValues.put("religionId",member.getReligionId());
            contentValues.put("racialId",member.getRaciald());
            contentValues.put("isBpl",member.getIsBpl());
            contentValues.put("bplNumber",member.getBplNumber());
            contentValues.put("rationcardNrumber",member.getRationcardNrumber());
            contentValues.put("rsbycardNumber",member.getRsbycardNumber());
            contentValues.put("macardNumber",member.getMacardNumber());
            contentValues.put("updatedDate",dateFormat.format(date));
            if(member.getLattitudes()!=null) {
                contentValues.put("lattitudes", member.getLattitudes());
                contentValues.put("longitude", member.getLongitude());
            }
            if(member.getFaliyu()!=null){
                contentValues.put("faliyaId", member.getFaliyu());
            }
            mDataBase.update(DBConstant.FAMILY_TABLE, contentValues, " emamtafamilyId='" + member.getEmamtafamilyId() + "'", null);
        }catch (Exception e) {
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
                    "macardNumber,lattitudes,longitude,createdbyuserId,createdDate" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, member.getEmamtafamilyId());
            insert.bindString(2, member.getVillageId());
            insert.bindString(3, member.getSubCenterId());
            if(member.getHouseNumber()!=null){
                insert.bindString(4, member.getHouseNumber());
            }
            insert.bindString(5, member.getFaliyu());
            if(member.getLandmark()!=null){
                insert.bindString(6, member.getLandmark());
            }
            insert.bindString(7, member.getRaciald());
            insert.bindString(8, member.getReligionId());
            insert.bindString(9, member.getIsBpl());
            if(member.getBplNumber()!=null) {
                insert.bindString(10, member.getBplNumber());
            }
            if(member.getRationcardNrumber()!=null) {
                insert.bindString(11, member.getRationcardNrumber());
            }
            if(member.getRsbycardNumber()!=null) {
                insert.bindString(12, member.getRsbycardNumber());
            }
            if(member.getMacardNumber()!=null){
                insert.bindString(13, member.getMacardNumber());
            }

            if(member.getLattitudes()!=null) {
                insert.bindString(14, member.getLattitudes());
                insert.bindString(15, member.getLongitude());
            }
            insert.bindString(16, member.getUserId());
            insert.bindString(17, dateFormat.format(date));
            insert.execute();
            mDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (mDataBase != null)
            {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public boolean createMember(Member member){

        try {
            Member lastMember=getLastInsertedFamlily();

            mDataBase = getWritableDatabase();
            mDataBase.beginTransaction();

            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(familyId,emamtafamilyId,villageId,photo," +
                    "firstName,middleName,lastName,isHead,gender,maritalStatus,birthDate,mobileNo,createdbyuserId," +
                    "createdDate" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            insert.bindString(1, lastMember.getFamilyId());
            insert.bindString(2, lastMember.getEmamtafamilyId());
            insert.bindString(3, member.getVillageId());
            if(member.getUserImageArray()!=null) {
                insert.bindBlob(4, member.getUserImageArray());
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
            insert.execute();
            mDataBase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (mDataBase != null)
            {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public Member getLastInsertedFamlily(){

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
        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }finally
        {
            if (mDataBase != null)
            {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return member;
    }

    public ArrayList<Family> searchFamily(String number,String villageId) {
//        number="FM/2009/7339895";
        ArrayList<Family> familyArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + number + "%' and m.isHead='1' and fm.villageId='" + villageId + "'";
            Log.e("Member Search", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }

    public Member getMemberDetail(String MemberId){

        Member member=new Member();
        readDatabase();
        try {
            String selectQuery="select m.memberId,m.familyId,m.emamtafamilyId,m.villageId,m.firstName,m.middleName,m.lastName,m.isHead,m.gender," +
                    "m.maritalStatus,m.birthDate,m.mobileNo,f.houseNumber,m.childof," +
                    "f.faliyaId,f.landmark,f.religionId,f.racialId,f.isBpl,f.lattitudes,f.longitude,f.bplNumber,f.rationcardNrumber," +
                    "f.rsbycardNumber, f.macardNumber " +
                    "from tbl_member as m inner join tbl_family as f on f.emamtafamilyId=m.emamtafamilyId " +
                    "where memberId='"+MemberId+"'";
            Log.v("MemberDetail Query", selectQuery);
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

                }while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return member;
    }



    public ArrayList<MaritalStatus> getMaritalStatus() {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();
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

        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public ArrayList<MaritalStatus> getRelation() {

        ArrayList<MaritalStatus> maritalStatusArrayList = new ArrayList<>();
        readDatabase();
        try {
            String selectQuery = "SELECT  * FROM relation_with_head";
            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    MaritalStatus maritalStatus = new MaritalStatus();
                    maritalStatus.setId(cursor.getString(cursor.getColumnIndex("relation_with_head_id")));
                    maritalStatus.setStatus(cursor.getString(cursor.getColumnIndex("relation_with_head")));
                    maritalStatusArrayList.add(maritalStatus);
                } while (cursor.moveToNext());
            }


        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return maritalStatusArrayList;
    }

    public void deleteMemberData()
    {
        writeDatabase();
        try
        {
            mDataBase.delete(DBConstant.MEMBER_TABLE, null, null);
        }
        catch (Exception e)
        {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
    }

    public boolean insertMemberData(JSONArray jsonMemberArray){

        mDataBase = getWritableDatabase();
        mDataBase.beginTransaction();

        try {
            String sql = "Insert into " + DBConstant.MEMBER_TABLE + "(memberId,emamtahealthId,familyId,emamtafamilyId,villageId," +
                    "emrNumber,photo,firstName,middleName,lastName,firstnameEng,middlenameEng,lastnameEng,isHead,relationwithheadId," +
                    "gender,maritalStatus,birthDate,mobileNo,childof,wifeof,adoptedfpMethod,wantadoptedfpMethod,plannedfpMethod,isPregnant" +
                    ",wantChild,memberStatus,menstruationStatus,adharcardNumber,electioncardNumber,pancardNumber,drivingcardNumer,passportcardNumber" +
                    ",migratedemamtafamilyId,migratedemamtamemberId,createdbyuserId,createdDate,updatedDate" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonMemberArray.length(); i++) {
                JSONObject jsonObject=jsonMemberArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("memberId"));
                insert.bindString(2,jsonObject.getString("emamtahealthId"));
                insert.bindString(3,jsonObject.getString("familyId"));
                insert.bindString(4,jsonObject.getString("emamtafamilyId"));
                insert.bindString(5,jsonObject.getString("villageId"));
                insert.bindString(6,jsonObject.getString("emrNumber"));
                insert.bindString(7,jsonObject.getString("photo"));
                insert.bindString(8,jsonObject.getString("firstName"));
                insert.bindString(9,jsonObject.getString("middleName"));
                insert.bindString(10,jsonObject.getString("lastName"));
                insert.bindString(11,jsonObject.getString("firstnameEng"));
                insert.bindString(12,jsonObject.getString("middlenameEng"));
                insert.bindString(13,jsonObject.getString("lastnameEng"));
                insert.bindString(14,jsonObject.getString("isHead"));
                insert.bindString(15,jsonObject.getString("relationwithheadId"));
                insert.bindString(16,jsonObject.getString("gender"));
                insert.bindString(17,jsonObject.getString("maritalStatus"));
                insert.bindString(18,jsonObject.getString("birthDate"));
                insert.bindString(19,jsonObject.getString("mobileNo"));
                insert.bindString(20,jsonObject.getString("childof"));
                insert.bindString(21,jsonObject.getString("wifeof"));
                insert.bindString(22,jsonObject.getString("adoptedfpMethod"));
                insert.bindString(23,jsonObject.getString("wantadoptedfpMethod"));
                insert.bindString(24,jsonObject.getString("plannedfpMethod"));
                insert.bindString(25,jsonObject.getString("isPregnant"));
                insert.bindString(26,jsonObject.getString("wantChild"));
                insert.bindString(27,jsonObject.getString("memberStatus"));
                insert.bindString(28,jsonObject.getString("menstruationStatus"));
                insert.bindString(29,jsonObject.getString("adharcardNumber"));
                insert.bindString(30,jsonObject.getString("electioncardNumber"));
                insert.bindString(31,jsonObject.getString("pancardNumber"));
                insert.bindString(32,jsonObject.getString("drivingcardNumer"));
                insert.bindString(33,jsonObject.getString("passportcardNumber"));
                insert.bindString(34,jsonObject.getString("migratedemamtafamilyId"));
                insert.bindString(35,jsonObject.getString("migratedemamtamemberId"));
                insert.bindString(36,jsonObject.getString("createdbyuserId"));
                insert.bindString(37,jsonObject.getString("createdDate"));
                insert.bindString(38,jsonObject.getString("updatedDate"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        }catch (JSONException e){
            e.printStackTrace();
        }
        finally
        {
            if (mDataBase != null)
            {
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
            String sql = "Insert into " + DBConstant.FAMILY_TABLE + "(familyId,emamtafamilyId,villageId,migratedvillagId,scId," +
                    "houseNumber,faliyaId,landmark,racialId,religionId,isBpl,migrationtypeId,bplNumber,rationcardNrumber,rsbycardNumber," +
                    "macardNumber,mavatsalyacardNumber,lattitudes,longitude,createdbyuserId,createdDate,updatedDate,faliyaId" +
                    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = mDataBase.compileStatement(sql);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                insert.bindLong(1, jsonObject.getInt("familyId"));
                insert.bindString(2,jsonObject.getString("emamtafamilyId"));
                insert.bindString(3,jsonObject.getString("villageId"));
                insert.bindString(4,jsonObject.getString("migratedvillagId"));
                insert.bindString(5,jsonObject.getString("scId"));
                insert.bindString(6,jsonObject.getString("houseNumber"));
                insert.bindString(7,jsonObject.getString("faliyaId"));
                insert.bindString(8,jsonObject.getString("landmark"));
                insert.bindString(9,jsonObject.getString("racialId"));
                insert.bindString(10,jsonObject.getString("religionId"));
                insert.bindString(11,jsonObject.getString("isBpl"));
                insert.bindString(12,jsonObject.getString("migrationtypeId"));
                insert.bindString(13,jsonObject.getString("bplNumber"));
                insert.bindString(14,jsonObject.getString("rationcardNrumber"));
                insert.bindString(15,jsonObject.getString("rsbycardNumber"));
                insert.bindString(16,jsonObject.getString("macardNumber"));
                insert.bindString(17,jsonObject.getString("mavatsalyacardNumber"));
                insert.bindString(18,jsonObject.getString("lattitudes"));
                insert.bindString(19,jsonObject.getString("longitude"));
                insert.bindString(20,jsonObject.getString("createdbyuserId"));
                insert.bindString(21,jsonObject.getString("createdDate"));
                insert.bindString(22,jsonObject.getString("updatedDate"));
                insert.bindString(23,jsonObject.getString("faliyaId"));
                insert.execute();
            }
            mDataBase.setTransactionSuccessful();
        }catch (JSONException e){
            e.printStackTrace();
        }
        finally
        {
            if (mDataBase != null)
            {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public void deleteFamilyDetail() {
        writeDatabase();
        try
        {
            mDataBase.delete(DBConstant.FAMILY_TABLE, null, null);
        }
        catch (Exception e)
        {
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
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (mDataBase != null)
            {
                mDataBase.endTransaction();
                mDataBase.close();
            }
        }
        return true;
    }

    public ArrayList<Religion> getFaliyaList(String villageId) {

        ArrayList<Religion> religionArrayList = new ArrayList<>();
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


        }catch (Exception e) {
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
            String selectQuery = "SELECT m.memberId,fm.familyId,fm.emamtafamilyId,m.emamtafamilyId,m.firstName,m.middleName,m.lastName,m.photo\n" +
                    "FROM tbl_member m inner join tbl_family fm " +
                    "on m.emamtafamilyId=fm.emamtafamilyId " +
                    "WHERE m.emamtafamilyId LIKE '%" + searchString + "%' and fm.villageId='" + strVillageId + "'";
            Log.e("Member Search", selectQuery);

            Cursor cursor = mDataBase.rawQuery(selectQuery, null);
            Log.e("Member Search Count", "" + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Family family = new Family();
                    family.setMemberId(cursor.getString(cursor.getColumnIndex("memberId")));
                    family.setId(cursor.getString(cursor.getColumnIndex("familyId")));
                    family.setEmamtaFamilyId(cursor.getString(cursor.getColumnIndex("emamtafamilyId")));
                    family.setMemberName(cursor.getString(cursor.getColumnIndex("firstName")) + " " + cursor.getString(cursor.getColumnIndex("middleName")) + " " + cursor.getString(cursor.getColumnIndex("lastName")));
                    family.setUserImageArray(cursor.getBlob(cursor.getColumnIndex("photo")));
                    familyArrayList.add(family);
                } while (cursor.moveToNext());
            }
        }catch (Exception e) {
            Log.i("Exe : ", e.getMessage());
            e.printStackTrace();
        }
        closeDataBase();
        SQLiteDatabase.releaseMemory();
        return familyArrayList;
    }
}
