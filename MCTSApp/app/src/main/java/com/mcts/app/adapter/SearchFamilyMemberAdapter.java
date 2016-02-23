package com.mcts.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.activity.familyhealthsurvey.UpdateFamilyMemberActivity;
import com.mcts.app.customview.CustomToast;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.model.Family;
import com.mcts.app.model.MaritalStatus;
import com.mcts.app.utils.Messages;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Raj on 11/7/2015.
 */
public class SearchFamilyMemberAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<Family> familyArrayList;
    private String villageId, villageName, searchString;
    private String isParmenant = "0";
    private String talukaID,villageID,dirstID,stateId,subcentreId;

    public SearchFamilyMemberAdapter(Context mContext, ArrayList<Family> mFamilyArrayList, String strVillageId, String strVillageName, String mSearchString) {
        this.context = mContext;
        this.familyArrayList = mFamilyArrayList;
        this.villageId = strVillageId;
        this.villageName = strVillageName;
        this.searchString = mSearchString;
    }

    @Override
    public int getCount() {
        return familyArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return familyArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_member_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img_member = (ImageView) convertView.findViewById(R.id.img_member);
            viewHolder.txt_fm_member = (TextView) convertView.findViewById(R.id.txt_fm_member);
            viewHolder.txt_fm_number = (TextView) convertView.findViewById(R.id.txt_fm_number);
            viewHolder.txt_fmhealth_number = (TextView) convertView.findViewById(R.id.txt_fmhealth_number);
            viewHolder.txt_edit = (TextView) convertView.findViewById(R.id.txt_edit);
            viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.txt_delete);
            viewHolder.txt_migrate = (TextView) convertView.findViewById(R.id.txt_migrate);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
            viewHolder.txt_fm_number.setTypeface(type, Typeface.BOLD);
            viewHolder.txt_fm_member.setTypeface(type, Typeface.BOLD);

            Utils.findAllTextView(context, (ViewGroup) convertView.findViewById(R.id.ll_search_member));
            viewHolder.txt_edit.setOnClickListener(this);
            viewHolder.txt_delete.setOnClickListener(this);
            viewHolder.txt_migrate.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_fm_member.setText(familyArrayList.get(position).getMemberName());
        viewHolder.txt_fmhealth_number.setText(familyArrayList.get(position).getMemberId());
        viewHolder.txt_fm_number.setText(familyArrayList.get(position).getEmamtaFamilyId());
        viewHolder.txt_edit.setTag(familyArrayList.get(position).getMemberId() + "," + familyArrayList.get(position).getEmamtaFamilyId());
        viewHolder.txt_delete.setTag(familyArrayList.get(position).getEmamtahealthId());
        viewHolder.txt_migrate.setTag(familyArrayList.get(position).getEmamtaFamilyId() + "," +familyArrayList.get(position).getEmamtahealthId());

        /*if (familyArrayList.get(position).getUserImageArray() != null) {
            if(familyArrayList.get(position).getUserImageArray().length>5) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(familyArrayList.get(position).getUserImageArray(), 0, familyArrayList.get(position).getUserImageArray().length);
                viewHolder.img_member.setImageBitmap(bitmap);
            }else {
                viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
            }
        } else {
            viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
        }*/

        if (familyArrayList.get(position).getPhoto() != null) {
            if(familyArrayList.get(position).getPhoto().length()>5) {
                Uri uri = Uri.parse(familyArrayList.get(position).getPhotoValue());
                Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
                viewHolder.img_member.setImageBitmap(image_bitmap);
            }else {
                viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
            }
        } else {
            viewHolder.img_member.setImageResource(R.drawable.ic_launcher);
        }

        return convertView;
    }


    private static class ViewHolder {

        ImageView img_member;
        TextView txt_fm_number;
        TextView txt_fmhealth_number;
        TextView txt_fm_member;
        TextView txt_edit;
        TextView txt_delete;
        TextView txt_migrate;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_edit:
                TextView textView = (TextView) v;
                Intent intent = new Intent(context, UpdateFamilyMemberActivity.class);
                String str = textView.getTag().toString();
                final String value[] = str.split(",");
                intent.putExtra("MemberId", value[0]);
                intent.putExtra("emamtaFamilyId", value[1]);
                intent.putExtra("villageId", villageId);
                intent.putExtra("villageName", villageName);
                context.startActivity(intent);
                break;

            case R.id.txt_delete:
//                TextView deleteFam = (TextView) v;
//                DatabaseHelper databaseHelper = new DatabaseHelper(context);
//                String eHealthId = deleteFam.getTag().toString();
//                boolean result = databaseHelper.deleteMemberByHealthId(eHealthId);
//                if (!result) {
//                    CustomToast customToast = new CustomToast((Activity) context, "Invalid operation");
//                    customToast.show();
//                } else {
//                    familyArrayList = databaseHelper.searchFamilyMember(searchString, villageId);
//                    notifyDataSetChanged();
//                }

                final Dialog dialog = new Dialog(context);
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = mInflater.inflate(R.layout.custom_delete_dialog, null);
                Utils.findAllTextView(context, ((ViewGroup) view.findViewById(R.id.ll_delete)));
                final TextView txt_delete_title = (TextView) view.findViewById(R.id.txt_delete_title);
                txt_delete_title.setText(R.string.member_delete);
                TextView deleteFam=(TextView)v;
                final String eHealthid=deleteFam.getTag().toString();
                Button bt_delete_yes = (Button) view.findViewById(R.id.bt_delete_yes);
                Button bt_delete_no = (Button) view.findViewById(R.id.bt_delete_no);

                bt_delete_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bt_delete_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        boolean result = databaseHelper.deleteMemberByHealthId(eHealthid);
                        if (!result) {
                            String str=context.getResources().getString(R.string.family_delete_unsuccess);
                            CustomToast customToast=new CustomToast((Activity) context,str);
                            customToast.show();
                        } else {
                            String str=context.getResources().getString(R.string.family_delete_success);
                            CustomToast customToast=new CustomToast((Activity) context,str);
                            customToast.show();
                            familyArrayList = databaseHelper.searchFamilyMember(searchString, villageId);
                            notifyDataSetChanged();
                        }
                    }
                });


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(view);

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
                Display display = wm.getDefaultDisplay(); // getting the screen size of device
                Point size = new Point();
                display.getSize(size);
                int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
                int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

                int tempValue = 0;
                tempValue = ((size.x) * 200) / 1440;
                int width = size.x - tempValue;  // Set your widths
                int height = height1; // set your heights

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.width = width;
                lp.height = height;
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
                dialog.show();

                break;

            case R.id.txt_migrate:

                TextView eMamta=(TextView)v;
                String emamtastr = eMamta.getTag().toString();
                final String values[] = emamtastr.split(",");
                final Dialog migrateDialog = new Dialog(context);
                LayoutInflater migrateInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View migrateView=migrateInflater.inflate(R.layout.custom_migration_layout, null);
                Utils.findAllTextView(context, ((ViewGroup) migrateView.findViewById(R.id.ll_migrate)));
                final Spinner sp_dirst=(Spinner)migrateView.findViewById(R.id.sp_dirst);
                final Spinner sp_taluka=(Spinner)migrateView.findViewById(R.id.sp_taluka);
                final Spinner sp_migrate_village=(Spinner)migrateView.findViewById(R.id.sp_migrate_village);
                final RadioButton rdb_parmenant=(RadioButton)migrateView.findViewById(R.id.rdb_parmenant);
                final RadioButton rdb_temp=(RadioButton)migrateView.findViewById(R.id.rdb_temp);
                Button bt_migrate=(Button)migrateView.findViewById(R.id.bt_migrate);
                Button bt_cancel=(Button)migrateView.findViewById(R.id.bt_cancel);
                final LinearLayout ll_out_state=(LinearLayout)migrateView.findViewById(R.id.ll_out_state);
                final LinearLayout ll_migrate_type=(LinearLayout)migrateView.findViewById(R.id.ll_migrate_type);
                final LinearLayout ll_place=(LinearLayout)migrateView.findViewById(R.id.ll_place);
                final Spinner sp_migrate_place=(Spinner)migrateView.findViewById(R.id.sp_migrate_place);
                final LinearLayout ll_select_state=(LinearLayout)migrateView.findViewById(R.id.ll_select_state);
                final Spinner sp_state=(Spinner)migrateView.findViewById(R.id.sp_state);

                ll_out_state.setVisibility(View.VISIBLE);
                ll_migrate_type.setVisibility(View.GONE);
                ll_place.setVisibility(View.GONE);

               /* sp_migrate_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 2) {

                            ll_select_state.setVisibility(View.VISIBLE);
                            LinearLayout linearLayout = (LinearLayout) view;
                            TextView textView = (TextView) linearLayout.getChildAt(0);
                            stateId = textView.getTag().toString();
                        } else if (position == 1) {
                            ll_out_state.setVisibility(View.VISIBLE);
                            ll_select_state.setVisibility(View.GONE);

                        } else {
                            ll_out_state.setVisibility(View.VISIBLE);
                            ll_select_state.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                ArrayList<MaritalStatus> districtArray = databaseHelper.getDistrictData();
                StatusAdapter districtAdapter = new StatusAdapter(context, districtArray);
                sp_dirst.setAdapter(districtAdapter);

                sp_dirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    LinearLayout linearLayout;
                    TextView textView;

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        linearLayout = (LinearLayout) view;
                        textView = (TextView) linearLayout.getChildAt(0);
                        dirstID = textView.getTag().toString();

                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        ArrayList<MaritalStatus> districtArray = databaseHelper.getTaluka(dirstID);
                        StatusAdapter districtAdapter = new StatusAdapter(context, districtArray);
                        sp_taluka.setAdapter(districtAdapter);

                        sp_taluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                linearLayout = (LinearLayout) view;
                                textView = (TextView) linearLayout.getChildAt(0);
                                talukaID = textView.getTag().toString();

                                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                                ArrayList<MaritalStatus> districtArray = databaseHelper.getVillages(talukaID);
                                StatusAdapter districtAdapter = new StatusAdapter(context, districtArray);
                                sp_migrate_village.setAdapter(districtAdapter);

                                sp_migrate_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if(position!=0) {
                                            linearLayout = (LinearLayout) view;
                                            textView = (TextView) linearLayout.getChildAt(0);
                                            String[] villageArray = textView.getTag().toString().split(",");
                                            villageID = villageArray[0];
                                            subcentreId = villageArray[1];
                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                rdb_parmenant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isParmenant = "0";
                        rdb_parmenant.setChecked(true);
                        rdb_temp.setChecked(false);
                    }
                });
                rdb_temp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isParmenant ="1";
                        rdb_parmenant.setChecked(false);
                        rdb_temp.setChecked(true);
                    }
                });

                bt_migrate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            boolean flag = databaseHelper.migrateMember(values[0], values[1], villageID,subcentreId, "0");
                            if (!flag) {
                                String str = context.getResources().getString(R.string.migrate_not_success);
                                CustomToast customToast = new CustomToast((Activity) context, str);
                                customToast.show();
                                migrateDialog.dismiss();
                            } else {
                                migrateDialog.dismiss();
                                String str = context.getResources().getString(R.string.migrate_success);
                                CustomToast customToast = new CustomToast((Activity) context, str);
                                customToast.show();
                                familyArrayList = databaseHelper.searchFamily(searchString, villageId);
                                notifyDataSetChanged();
                        }
                    }
                });

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        migrateDialog.dismiss();
                    }
                });

                migrateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                migrateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                migrateDialog.setContentView(migrateView);

                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
                Display migrateDisplay = windowManager.getDefaultDisplay(); // getting the screen size of device
                Point migrateSize = new Point();
                migrateDisplay.getSize(migrateSize);
                int migratewidth1 = WindowManager.LayoutParams.WRAP_CONTENT;
                int migrateheight1 = WindowManager.LayoutParams.WRAP_CONTENT;

                int migrateTempValue = 0;
                migrateTempValue = ((migrateSize.x) * 200) / 1440;
                int migrateWidth = migrateSize.x - migrateTempValue;  // Set your widths
                int migrateheight = migrateheight1; // set your heights

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(migrateDialog.getWindow().getAttributes());

                layoutParams.width = migrateWidth;
                layoutParams.height = migrateheight;
                migrateDialog.getWindow().setAttributes(layoutParams);
                migrateDialog.setCancelable(false);
                migrateDialog.show();

                break;
        }
    }

}
