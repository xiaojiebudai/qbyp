package cn.net.chenbao.qbypseller.imageSelector;

import java.util.List;

/**
 * 文件夹
 */
public class Folder {
	public String path;
	public String name;
	public Image cover;
	public List<Image> images;

	@Override
	public boolean equals(Object o) {
		try {
			Folder other = (Folder) o;
			return this.path.equalsIgnoreCase(other.path);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return super.equals(o);
	}
}
