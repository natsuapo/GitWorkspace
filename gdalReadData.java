package com.GDAL.gdalforandroid;

import java.util.ArrayList;

import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

import android.R.bool;
import android.R.integer;
import android.graphics.Point;
import android.util.Log;
import android.widget.TextView;


public class gdalReadData {
	ArrayList<Layer> layerList = new ArrayList<Layer>();
	ArrayList<Feature> featureList = new ArrayList<Feature>();
	ArrayList<Geometry> geometryList = new ArrayList<Geometry>();
	
	public void GetLayerFromDs(DataSource ds)
	{
		int layerCount = ds.GetLayerCount();
		for(int i=0;i<layerCount;i++)
		{
			Layer pLayer = ds.GetLayer(i);
			layerList.add(pLayer);
		}
		
/*		while((pFeature = pLayer.GetNextFeature())!=null)
		{
			FeatureDefn pDefn = pLayer.GetLayerDefn();
			Geometry pGeometry = pFeature.GetGeometryRef();
			if(pGeometry!=null&& wkbFlatten(pGeometry.GetGeometryType()==))
			
		}*/
		

	}
	
//	public void saveFile(String Route, )
	
	
	public ArrayList<Feature> GetFeatureFromLayer(Layer pLayer)
	{
		featureList.clear();
		geometryList.clear();
		Feature pFeature;
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		while((pFeature = pLayer.GetNextFeature())!=null)
		{
			featureList.add(pFeature);
			featureArrayList.add(pFeature);
			Geometry pGeometry = pFeature.GetGeometryRef();
			geometryList.add(pGeometry);
		}
		return  featureArrayList;
	}
	
	public ArrayList<Feature> attributeQuery(String SQL,Layer pLayer)
	{
		featureList.clear();
		geometryList.clear();
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		Feature pFeature;
		pLayer.SetAttributeFilter(SQL);
		while((pFeature = pLayer.GetNextFeature())!=null)
		{
			featureList.add(pFeature);
			Geometry pGeometry = pFeature.GetGeometryRef();
			geometryList.add(pGeometry);
			featureArrayList.add(pFeature);
		}
		return featureArrayList;
	}
	
	public ArrayList<Feature> envelopeQuery(double minx,double miny, double maxx,double maxy,Layer pLayer)
	{
		pLayer.SetSpatialFilterRect(minx, miny, maxx, maxy);
		Feature pFeature;
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		while((pFeature = pLayer.GetNextFeature())!=null)
			featureArrayList.add(pFeature);
		return featureArrayList;
	}

	//用这个函数实现点查询
	public ArrayList<Feature> spatialQuery(Geometry pGeometry,Layer pLayer)
	{
		pLayer.SetSpatialFilter(pGeometry);
		Feature pFeature;
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		while((pFeature = pLayer.GetNextFeature())!=null)
			featureArrayList.add(pFeature);
		return featureArrayList;
	}
	
	public ArrayList<Feature> getintersectArrayList(Geometry pGeometry,Layer pLayer)
	{
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		Feature pFeature;
		pLayer.ResetReading();
		int count = pLayer.GetFeatureCount();
		for(int i=0;i<count;i++)
		{
			pFeature = pLayer.GetFeature(i);

//			if(pGeometry.Within(pFeature.GetGeometryRef()))
//				featureArrayList.add(pFeature);
			if(GeosFunction.ScanIntersection(pGeometry, pFeature.GetGeometryRef())==true)
			{
				Log.e("get", "intersected");
				featureArrayList.add(pFeature);
			}
		}
		return featureArrayList;
	}
	
	public ArrayList<Feature> getwithinArrayList(Geometry pGeometry,Layer pLayer)
	{
		ArrayList<Feature> featureArrayList = new ArrayList<Feature>();
		Feature pFeature;
		while((pFeature = pLayer.GetNextFeature())!=null)
		{
//			if(pGeometry.Within(pFeature.GetGeometryRef()))
//				featureArrayList.add(pFeature);
			if(GeosFunction.pointWithinpolygon(pGeometry, pFeature.GetGeometryRef()))
			{
				Log.e("get", "intersected");
				featureArrayList.add(pFeature);
			}
		}
		return featureArrayList;
	}
	
	public ArrayList<Geometry> getGeometry(ArrayList<Feature> pArrayList)
	{
		ArrayList<Geometry> pGeometry = new ArrayList<Geometry>();
		for(int i=0;i<pArrayList.size();i++)
			pGeometry.add(pArrayList.get(i).GetGeometryRef());
		return pGeometry;
	}
 	
		
}
