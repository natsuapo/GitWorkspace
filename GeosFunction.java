package com.GDAL.gdalforandroid;

import java.util.ArrayList;
import java.util.SortedMap;

import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;

import android.R.bool;
import android.R.integer;
import android.graphics.Point;
import android.util.Log;

public class GeosFunction {

	public static boolean pointWithinpolygon(Geometry pPointGeometry,Geometry pPolygonGeometry )
	{
		double[] pPoint2D = new double[2];
//		Geometry pPolygonGeometry = pPolygonFeature.GetGeometryRef();
		pPoint2D = pPointGeometry.GetPoint_2D(0);
		double angleSum = 0; 
//		int GeometryCount = pPolygonGeometry.GetGeometryCount();
		Geometry pGeometryDef = pPolygonGeometry.GetGeometryRef(0);
		int Count = pPolygonGeometry.GetGeometryRef(0).GetPointCount();
		for(int i = 0;i<Count;i++)
		{
			angleSum += getAngle(pGeometryDef.GetPoint_2D(i%Count), pGeometryDef.GetPoint_2D((i+1)%Count), pPoint2D);
		}
		
		if(angleSum == 2*Math.PI)
			return true;
		else
			return false;
	}
	
	public static boolean ScanIntersection(Geometry pPointGeometry,Geometry pPolygonGeometry)
	{
		double[] pPoint2D = new double[2];
		pPoint2D = pPointGeometry.GetPoint_2D(0);
		double pointY = pPoint2D[1];
		double pointX = pPoint2D[0];
		Geometry pGeometryDef = pPolygonGeometry.GetGeometryRef(0);
		int Count = pPolygonGeometry.GetGeometryRef(0).GetPointCount();
		int IntersectCount = 0;
		for(int i =0;i<Count;i++)
		{
			double[] preNode = pGeometryDef.GetPoint_2D(i%Count);
			double[] postNode = pGeometryDef.GetPoint_2D((i+1)%Count);
			//�ж�ɨ�����Ƿ��ཻ
			if((pointY-preNode[1])*(pointY-postNode[1]) <= 0 )
			{
				//�жϵ���ɨ���ߵ���һ�ࡣ
				double IntersectP = GetXCoor(preNode,postNode ,pointY);
				if(IntersectP == pointX)
					//������е���ĳһ�����ϣ����ж�Ϊ���ڶ�����ڡ�
					return true;
				else if(IntersectP < pointX)
				{
					//��������ߵ����Σ����н�һ��������
					if((IntersectP == preNode[0])&&(IntersectP == postNode[0]))
						//ƽ�е�������迼�ǡ�
						continue;
					else if((IntersectP == preNode[0])||(IntersectP==postNode[0]))
						//��������˵㣬���ڶ˵��ǽϴ��������+1��
						if(pointY == Math.max(preNode[1],postNode[1]))
							IntersectCount = IntersectCount+1;
						else 
							continue;
					else
						IntersectCount = IntersectCount+1;
				}
			}
		}
		
		if (IntersectCount%2 !=0)
		{
			Log.e("Get", "Intersect");
			return true;
		}
		else 
			return false;
	}
	
	public static double GetXCoor(double[] point1, double[] point2, double YCoor)
	{
		double dy = point1[1]-point2[1];
		double dx = point1[0]-point2[0];

		if(dy == 0)
			return point1[0];
		else
		{
			double cot = dx/dy;
			return  (YCoor-point1[1])*cot + point1[0];
		}
	}
	 
	public static double GetYCoor(double[] point1, double[] point2, double XCoor)
	{
		double dy = point1[1]-point2[1];
		double dx = point1[0]-point2[0];
		if(dx == 0)
			return point1[1];
		else
		{
			double tan = dy/dx;
			return  (XCoor-point1[0])*tan + point1[1];
		}
	}
	
	public static double getLength(double[] point1, double[] point2)
	{
		return Math.sqrt(Math.pow(point1[0]- point2[0], 2)+Math.pow(point1[1]- point2[1], 2));
	}
	
	
	public static double getAngle(double[] point1,double[] point2,double[] point)
	{
		double length1 = getLength(point1, point);
		double length2 = getLength(point2, point);
		double dx1 = point1[0] - point[0];
		double dx2 = point2[0] - point[0];
		double dy1 = point1[1] - point[1];
		double dy2 = point2[1] - point[1];
		double dotProduct = dx1*dx2 + dy1*dy2;
		double cosDP = dotProduct / (length1*length2);
		return Math.acos(cosDP);
	}
	
	public boolean getinteresctArray( )
	{
		return true;
	}
	
	public boolean pointWithinMBR(Feature pPointFeature)
	{
		return true;
	}
	
}
