package org.dlug.disastercenter.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.xenix.image.lib.ImageProcessing;

import org.dlug.disastercenter.utils.FileUtils;
import org.dlug.disastercenter.utils.Trace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;

public class TakePictureManager {
	private final static int PICK_FROM_CAMERA = 1004;
	private final static int PICK_FROM_ALBUM  = 1005;
	
	public final static int MENU_CAMERA = 0;
	public final static int MENU_ALBUM  = 1;
	public final static int MENU_DELETE = 2;
	
	
	private final static int CROP_IMAGE = 1006;
	
	private TakePictureManagerDelegate mDelegate;
	private Activity mActivity;
	private Fragment mFragment;
	private Uri mImageCaptureUri;
	private boolean mIsCrop   = false;
	private boolean mIsRotate = false;
	private int mCropWidth, mCropHeight;
	
	
	public TakePictureManager(Activity activity) {
		mActivity = activity;
		mCropWidth  = 100;
		mCropHeight = 100;
	}
	
	public TakePictureManager(Fragment fragment) {
		mFragment = fragment;
		mCropWidth  = 100;
		mCropHeight = 100;
	}
	
	
	public void setDelegate(TakePictureManagerDelegate delegate) {
		mDelegate = delegate;
	}
	
	public TakePictureManagerDelegate getDelegate() {
		return mDelegate;
	}
	
	
	
	public void setCrop(boolean crop) {
		mIsCrop = crop;	
	}
	
	public void setRotateEnabled(boolean enabled) {
		mIsRotate = enabled;
	}
	
	public void setCropSize(int width, int height) {
		mCropWidth  = width;
		mCropHeight = height;
	}
	
	public void showMenuDialog(boolean withDelete) {
		showMenuDialog(getContext(), this, withDelete, null);		
	}
	
	
	public static void showMenuDialog(Context context, boolean withDelete, DialogInterface.OnClickListener listener) {
		showMenuDialog(context, null, withDelete, listener);
	}
	
	private static void showMenuDialog(Context context, final TakePictureManager manager, boolean withDelete, DialogInterface.OnClickListener listener) {
		ArrayList<String> takeMenu = new ArrayList<String>(3);
		
		takeMenu.add("사진 찍기");
		takeMenu.add("앨범에서 사진선택");
		
		if ( withDelete )
			takeMenu.add("삭제");
		
		ArrayAdapter<String> takePickAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, takeMenu);
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle("Photo");
		
		if ( listener != null ) {
			dialogBuilder.setAdapter(takePickAdapter, listener);
		}
		else {
			dialogBuilder.setAdapter(takePickAdapter, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch ( which ) {
					case MENU_CAMERA:		// 사진찍기
						if ( manager != null )
							manager.takePickFromCamera();
						break;
						
					case MENU_ALBUM:		// 사진가져오기
						if ( manager != null )
							manager.takePickFromAlbum();
						break;
						
					case MENU_DELETE:
						if ( manager != null ) {
							TakePictureManagerDelegate delegate = manager.getDelegate();
							if ( delegate != null )
								delegate.onDeletePickture();
						}
						break;
					}
				}
			});
		}
		dialogBuilder.create().show();
	}


	public void takePickFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
        mImageCaptureUri = getImageFileUri("jpg");
        
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
	}
	
	public void takePickFromAlbum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        
        startActivityForResult(intent, PICK_FROM_ALBUM);
	}
	
	private Uri getImageFileUri(String extension) {
		String path = null;
		Context context = getContext();
		
		path = context.getExternalFilesDir(null).getAbsolutePath();
		
		if ( !path.endsWith(File.separator) ) {
			path += File.separator;
		}
        String url  = "takePicture" + "." + extension;
        return Uri.fromFile(new File(path, url));	
	}
	
	private File getImageFileFromUri(Uri imgUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        if (imgUri == null) {
        	imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        
        Context context = getContext();
        
        Cursor mCursor = context.getContentResolver().query(imgUri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();
 
        String path = mCursor.getString(column_index);
 
        if (mCursor != null ) {
            mCursor.close();
            mCursor = null;
        }
 
        return new File(path);
	}
	

	
	
	

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if ( resultCode != Activity.RESULT_OK ) {
			if ( mDelegate != null )
				mDelegate.onTakePicktureCancel();
			return ;
		}
		
		switch ( requestCode ) {
		case PICK_FROM_ALBUM:
			try {
				Uri imgUri   = data.getData();
				File srcFile = getImageFileFromUri(imgUri);
				String extension = MimeTypeMap.getFileExtensionFromUrl(srcFile.getName());
				
				mImageCaptureUri = getImageFileUri(extension);
				File destFile = new File(mImageCaptureUri.getPath());
				
				FileUtils.copyFile(srcFile, destFile);
			} catch (Exception e) {
				if ( mDelegate != null )
					mDelegate.onTakePicktureError();
			}
			
		case PICK_FROM_CAMERA:
			if ( mIsCrop ) {
	            Intent intent = new Intent("com.android.camera.action.CROP");
	            intent.setDataAndType(mImageCaptureUri, "image/*");
	
	            // Crop한 이미지를 저장할 Path
	            intent.putExtra("output", mImageCaptureUri);
	            intent.putExtra("outputX", mCropWidth);
	            intent.putExtra("outputY", mCropHeight);
	            
	            intent.putExtra("aspectX", 1);
	            intent.putExtra("aspectY", 1);
	            startActivityForResult(intent, CROP_IMAGE);
	            
			}
			else {
				if ( mIsRotate ) {
					try {
						String imgPath = mImageCaptureUri.getPath();
						int exifDegree = exifOrientationToDegrees(imgPath);

						File f = new File(imgPath);
						String savePath = getContext().getExternalFilesDir(null).getAbsolutePath();
						if ( !savePath.endsWith(File.separator) ) {
							savePath = savePath + File.separator;
						}
						
						savePath = savePath + f.getName();
						if ( exifDegree != 0 ) {
							ImageProcessing.rotateImage(imgPath, savePath, exifDegree);
						}
						else {
							savePath = imgPath;
						}
						
						mImageCaptureUri = Uri.parse(savePath);
						
					} catch (Exception e) {
						Trace.Error("Bitmap rotate fail: " + e.toString());
					}
				}
				
				if ( mDelegate != null ) 
					mDelegate.onTakePickture(mImageCaptureUri);
				
			}
			
			break;
			
		case CROP_IMAGE:
			if ( mDelegate != null )
				mDelegate.onTakePickture(mImageCaptureUri);
			break;
		}
		
	}
	
	private void startActivityForResult(Intent intent, int requestCode) {
		if (mActivity != null) {
			mActivity.startActivityForResult(intent, requestCode);
		} 
		else if (mFragment != null) {
			mFragment.startActivityForResult(intent, requestCode);
		}
	}

	private Context getContext() {
		Context context = mActivity;
		
		if ( context == null && mFragment != null ) 
			context = mFragment.getActivity();
		
		return context;
	}
	
	private int exifOrientationToDegrees(String filePath) {
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) 
				return 90;
			
			else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
				return 180;
			
			else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
				return 270;
			
		} catch (IOException e) {
			Trace.Error("exif get: " + e.toString());
		}
		
		
		return 0;
	}
	
	public interface TakePictureManagerDelegate {
		public void onTakePickture(Uri imageUri);
		public void onTakePicktureCancel();
		public void onTakePicktureError();
		public void onDeletePickture();
	}
	 
	 
}
