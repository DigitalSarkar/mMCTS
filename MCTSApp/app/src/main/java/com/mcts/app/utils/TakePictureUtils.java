package com.mcts.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

  /** this class is used for image operation */

public  class TakePictureUtils {
	
	public static final int TAKE_PICTURE = 1; 
	public static final int PICK_GALLERY = 2; 
	public static final int CROP_FROM_CAMERA = 3;
	public static ContentResolver mContentResolver = null;
	public static final int IMAGE_MAX_SIZE = 1024;
	 /** this method is used for take picture from camera */
	public static void takePicture(Activity context, String fileName) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {

			Uri mImageCaptureUri = null;
//			Log.e(context + "", "file name " + fileName);
			mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".png"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
			intent.putExtra("return-data", true);
			context.startActivityForResult(intent, TAKE_PICTURE);

		} catch (ActivityNotFoundException e) {
//			Log.e(context + "", "cannot take picture " + e);

		} catch (Exception ex) {

//			Log.e(context + "", "cannot take picture " + ex);
		}
	}

	 
	 /** this method is used for take picture from gallery */
	 public static void openGallery(Activity context) {
	    	
	        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	        photoPickerIntent.setType("image/*");
	        context.startActivityForResult(photoPickerIntent, PICK_GALLERY);
	    }
    
	    
	 
	  /** this method is used for open crop image */
//	public static void startCropImage(Activity context, String fileName) {
//	    	
//	        Intent intent = new Intent(context, CropImage.class);
//	        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp") ,fileName).getPath());
//	        intent.putExtra(CropImage.SCALE, true);
//	        intent.putExtra(CropImage.ASPECT_X, 1);
//	        intent.putExtra(CropImage.ASPECT_Y, 1);
//	        intent.putExtra(CropImage.OUTPUT_X, 800);
//	        intent.putExtra(CropImage.OUTPUT_Y, 600);
//	        context.startActivityForResult(intent, CROP_FROM_CAMERA);
//	    }
	 
	
	 /** this method is used for copy stream */
	
	 public static void copyStream(InputStream input, OutputStream output)
	            throws IOException {

	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	        }
	    }
	 
	//TODO: Mehul To set height and width of receipt
	 public static Bitmap RotateBitmap(Bitmap source, float angle)
	 {
	       Matrix matrix = new Matrix();
	       matrix.postRotate(angle);
	       return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	 }
	public static Bitmap getScaledBitmap(String picturePath, int width,
			int height) {
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, sizeOptions);

		int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

		sizeOptions.inJustDecodeBounds = false;
		sizeOptions.inSampleSize = inSampleSize;

		return BitmapFactory.decodeFile(picturePath, sizeOptions);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	public static File catchImage(Bitmap bitmap, Activity context, String filename){
//		Display display = context.getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		int width = size.x;
//		int height = size.y;
		
		File f = new File(context.getCacheDir(), "receipt");
		//Convert bitmap to byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 80, bos);
//		bitmap = TakePictureUtils.RotateBitmap(bitmap, 90);
		byte[] bitmapdata = bos.toByteArray();

		//write the bytes in file
		
		try {
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bitmapdata);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
	
	public static Bitmap getBitmap(String path, Activity context) {

        Uri uri = getImageUri(path);
        Log.e("uri: ",""+uri);
        mContentResolver = context.getContentResolver();
        InputStream in = null;
        try {
            in = mContentResolver.openInputStream(uri);

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            return b;
        } catch (FileNotFoundException e) {
//            Log.e(TAG, "file " + path + " not found");
        } catch (IOException e) {
//            Log.e(TAG, "file " + path + " not found");
        }
        return null;
    }
	
	//TODO: Mehul - decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f) {
		int orientation;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 800;
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);
			ExifInterface exif = new ExifInterface(f.getPath());
			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			// Log.e("ExifInteface .........", "rotation ="+orientation);
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				// Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				// Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				// Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
			// return BitmapFactory.decodeStream(new FileInputStream(f), null,
			// o2);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	public static Uri getImageUri(String path) {

        return Uri.fromFile(new File(path));
    }
	

	 
}
