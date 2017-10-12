package cn.net.chenbao.qbypseller.bean;

/**
 * 地区
 * 
 * @author licheng
 *
 */
public class Region {

	public int Id;
	public double Latitude;
	public double Longitude;
	public int Level;
	public String Name;
	public int ParentId;
	public String ShortName;
	public int SortId;
	public boolean isSelect;

	@Override
	public String toString() {
		return "Region [Id=" + Id + ", Latitude=" + Latitude + ", Longitude="
				+ Longitude + ", Level=" + Level + ", Name=" + Name
				+ ", ParentId=" + ParentId + ", ShortName=" + ShortName
				+ ", SortId=" + SortId + "]";
	}
}
