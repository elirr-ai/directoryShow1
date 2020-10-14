package com.example.directoryshow1;

import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.util.Log;

public class MSTgetCameraParams {
static boolean cameraFront=false;
static Camera.Parameters param =null;
static Camera camera=null;
static StringBuilder sb=new StringBuilder();
static final	String nl="\n";

	public  static String getCameraParameters(){
		
//	Camera camera = Camera.open();
//	if (camera.getParameters()!=null) {
//	Camera.Parameters param = camera.getParameters();
	sb.append("Number of cameras: "+Camera.getNumberOfCameras()+nl+nl);
	
    for (int i = 0; i < Camera.getNumberOfCameras(); i++) {   	
    	sb.append(nl+nl+"-------------------------------"+"    Camera number:  "+(i+1)+"    -------------------------------"+nl+nl);   	
        CameraInfo infoA = new CameraInfo();
        Camera.getCameraInfo(i, infoA);      
        if (infoA.facing == CameraInfo.CAMERA_FACING_FRONT) {
        	if (Camera.getNumberOfCameras()>1) {	
        		if (camera!=null){
        			camera.release();
        				}
    		camera = Camera.open(findFrontFacingCamera());
    		if (camera.getParameters()!=null) {
            	sb.append(nl+nl+"Camera Facing: FRONT"+nl+nl);
            	param = camera.getParameters();
            	getAllCameraparams();
        	}
    	}
        	}
        else     if (infoA.facing == CameraInfo.CAMERA_FACING_BACK) {
            	if (Camera.getNumberOfCameras()>1) {	
               		if (camera!=null){
            			camera.release();
            		}
            		camera = Camera.open(findBackFacingCamera());
            		if (camera.getParameters()!=null) {
                    	sb.append(nl+nl+"Camera Facing: BACK"+nl+nl);
                    	param = camera.getParameters(); 
                    	getAllCameraparams();
                	}
            	}          	
        }
      }
	if (camera!=null){
		camera.release();
	}
//	camera.release();
	return sb.toString();
	}

	private static void getAllCameraparams() {

		if (param.getFlashMode()!=null) sb.append("FlashMode: "+param.getFlashMode()+nl+nl);
		if (param.getFocusMode()!=null) sb.append("FocusMode: "+param.getFocusMode()+nl+nl);
		if (param.getColorEffect()!=null) sb.append("ColorEffect: "+param.getColorEffect()+nl+nl);
		if (param.getSceneMode()!=null) sb.append("getSceneMode: "+param.getSceneMode()+nl+nl);
		if (param.getAntibanding()!=null) sb.append("Antibanding: "+param.getAntibanding()+nl+nl);
		sb.append("AutoExposureLock: "+param.getAutoExposureLock()+nl+nl);
		sb.append("AutoWhiteBalanceLock: "+param.getAutoWhiteBalanceLock()+nl+nl+nl);
		sb.append("ExposureCompensation: "+param.getExposureCompensation()+nl+nl);
		sb.append("ExposureCompensationStep: "+param.getExposureCompensationStep()+nl+nl);
		sb.append("FocalLength: "+param.getFocalLength()+nl+nl);
		sb.append("HorizontalViewAngle: "+param.getHorizontalViewAngle()+nl+nl);
		sb.append("JpegQuality: "+param.getJpegQuality()+nl+nl);
		sb.append("MaxExposureCompensation: "+param.getMaxExposureCompensation()+nl+nl);
	 	sb.append("SceneMode: "+param.getSceneMode()+nl+nl);
		sb.append("MaxNumFocusAreas: "+param.getMaxNumFocusAreas()+nl+nl);
		sb.append("MaxNumMeteringAreas: "+param.getMaxNumMeteringAreas()+nl+nl);
	 	sb.append("MaxZoom: "+param.getMaxZoom()+nl+nl);
		sb.append("MinExposureCompensation: "+param.getMinExposureCompensation()+nl+nl);
		sb.append("PictureFormat: "+param.getPictureFormat()+nl+nl);
		sb.append("Picture size: "+param.getPictureSize().height+" x "+param.getPictureSize().width+nl+nl);
//		sb.append("FocusAreas: "+getParesedList(param.getFocusAreas()+nl));
	sb.append("supAnti: "+getParesedList(param.getSupportedAntibanding())+nl+nl);
	sb.append("ColorEffects: "+getParesedList(param.getSupportedColorEffects())+nl+nl);
	sb.append("FlashModes: "+getParesedList(param.getSupportedFlashModes())+nl+nl);
	sb.append("FocusModes: "+getParesedList(param.getSupportedFocusModes())+nl+nl);
	sb.append("SceneModes: "+getParesedList(param.getSupportedSceneModes())+nl+nl);
	sb.append("WhiteBalance: "+getParesedList(param.getSupportedWhiteBalance())+nl+nl);
	sb.append("JpegThumbnailSizes: "+getParesedListSize(param.getSupportedJpegThumbnailSizes())+nl);
	sb.append("SupportedPictureFormats: "+getParesedListInt(param.getSupportedPictureFormats())+nl);

	//sb.append("SupportedVideoFormats: "+getParesedListSize(param.getSupportedVideoSizes())+nl);

	sb.append("PreviewFormats: "+getParesedListInt(param.getSupportedPreviewFormats())+nl);
	sb.append("VideoSizes: "+getParesedListSize(param.getSupportedVideoSizes())+nl);
	sb.append("PictureSizes: "+getParesedListSize(param.getSupportedPictureSizes())+nl);

	//sb.append("FpsRange: "+getParesedListSize(param.getSupportedPreviewFpsRange())+nl);
	List<int[]>  FpsRange =param.getSupportedPreviewFpsRange();
	sb.append("FpsRange: "+getParesedListArrayOfIntegers(FpsRange)+nl);

		
		
	}

	private static String getParesedList(List<String> area) {
		StringBuilder sbb=new StringBuilder();
		String s="";
		if (area!=null){
			for (int i=0;i<area.size();i++){
				if (i==0) sbb.append("\n");
				sbb.append(""+"\t\t"+area.get(i)+"\n");
			}
			s=sbb.toString();
		}
		return s;
	}
	
	private static String getParesedListInt(List<Integer> in) {
		StringBuilder sbb=new StringBuilder();
		String s="";
		if (in!=null){
			for (int i=0;i<in.size();i++){
				sbb.append(""+"\t\t"+in.get(i)+" , ");
			}
			s=sbb.toString();
		}
		return s;
	}
	
	private static String getParesedListSize(List<Size> in) {
		StringBuilder sbb=new StringBuilder();
		String s="";
		if (in!=null){
			for (int i=0;i<in.size();i++){
				if (i==0) sbb.append("\n");
				sbb.append(""+"\t\t"+in.get(i).height+" x "+in.get(i).width+"\n");
			}
			s=sbb.toString();
		}
		return s;
	}
	
	
	
	
	private static String getParesedListArrayOfIntegers(List<int[]> in) {
		StringBuilder sbb=new StringBuilder();
		String s="no thing there";
		if (in!=null){
			
			for (int i=0;i<in.size();i++){
				if (i==0) sbb.append("\n");
					int[]  y=in.get(i);
					for (int j=0;j<y.length;j++){
						sbb.append(""+"\t\t"+y[j]+" - ");
						if (j!=0) sbb.append(" x ");
					}

			}
			s=sbb.toString();
		}
		return s;
	}
	
	
	private static int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}

	private static int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}
	
	
}

//          	List<Size> tmpList = mCamera.getParameters().getSupportedPreviewSizes();
//if (mCamera == null) {
//	if (Camera.getNumberOfCameras()==2 && !sharedpreferences.getBoolean(FRONT, false)) {	
//		mCamera = Camera.open(findFrontFacingCamera());
//	}
//	else if (Camera.getNumberOfCameras()==2 && sharedpreferences.getBoolean(FRONT, false)){ 
//		mCamera = Camera.open(findBackFacingCamera());
//					}
//}


