package nf.framework.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 解压zip包
 * 
 * @author niufei
 * 
 */
public class ZipUtil {

	public enum ZIPMODE {

		COVER // 覆盖
		, UPDATE; // 更新
	}

	/*************************** 压缩zip包 ***************************************/
	/**
	 * 压缩文件-由于out要在递归调用外,所以封装一个方法用来 调用ZipFiles(ZipOutputStream out,String
	 * path,File... srcFiles)
	 * 
	 * @param zip
	 * @param path
	 * @param srcFiles
	 * @throws IOException
	 * @author isea533
	 */
	public static void ZipFiles(File zip, String path, File... srcFiles)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		ZipUtil.ZipFiles(out, path, srcFiles);
		out.close();
		System.out.println("*****************压缩完毕*******************");
	}

	/**
	 * 压缩文件-File
	 * 
	 * @param zipFile
	 *            zip文件
	 * @param srcFiles
	 *            被压缩源文件
	 * @author isea533
	 */
	public static void ZipFiles(ZipOutputStream out, String path,
			File... srcFiles) {
		path = path.replaceAll("\\*", "/");
		if (!path.endsWith("/")) {
			path += "/";
		}
		byte[] buf = new byte[1024];
		try {
			for (int i = 0; i < srcFiles.length; i++) {
				if (srcFiles[i].isDirectory()) {
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if (!srcPath.endsWith("/")) {
						srcPath += "/";
					}
					out.putNextEntry(new ZipEntry(path + srcPath));
					ZipFiles(out, path + srcPath, files);
				} else {
					FileInputStream in = new FileInputStream(srcFiles[i]);
					// System.out.println(path + srcFiles[i].getName());
					out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**************************** 解压zip包 **************************************/
	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 *            文件路径名称
	 * @param descDir
	 *            解压路径名称
	 * @author isea533
	 */
	public static boolean unZipFiles(String zipPath, String descDir)
			throws IOException {
		return unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 *            文件路径名称
	 * @param descDir
	 *            解压路径名称
	 * @author isea533
	 */
	public static boolean unZipFiles(String zipPath, String descDir,
			ZIPMODE zipMode) throws IOException {
		return unZipFiles(new File(zipPath), descDir, zipMode, new String[]{});
	}

	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 *            文件路径名称
	 * @param descDir
	 *            解压路径名称
	 * @author isea533
	 */
	public static boolean unZipFiles(String zipPath, String descDir,
			ZIPMODE zipMode, String... exceptFiles) throws IOException {
		return unZipFiles(new File(zipPath), descDir, zipMode, exceptFiles);
	}

	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 *            文件路径名称
	 * @param descDir
	 *            解压路径名称
	 * @param exceptFile
	 *            除此文件不解压外
	 */
	public static boolean unZipFiles(String zipPath, String descDir,
			String... exceptFiles) throws IOException {
		return unZipFiles(new File(zipPath), descDir, exceptFiles);
	}

	/**
	 * 解压文件到指定目录 默认解压模式是覆盖
	 * 
	 * @param zipFile
	 * @param descDir
	 * @param exceptFileName
	 *            不解压的文件名称
	 * @return
	 * @throws IOException
	 */
	public static boolean unZipFiles(File zipFile, String descDir,
			String... exceptFileNames) throws IOException {
		return unZipFiles(zipFile, descDir, ZIPMODE.COVER, exceptFileNames);
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @param exceptFileName
	 *            不解压的文件名称
	 * @author isea533
	 */
	public synchronized static boolean unZipFiles(File zipFile, String descDir,
			ZIPMODE zipMode, String... exceptFileNames) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		if(!descDir.endsWith(File.separator)){
			descDir=descDir+File.separator;
		}
		ZipFile zip = new ZipFile(zipFile.getPath(), "gbk");
	
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			
			String zipEntryName = entry.getName();
			if (exceptFileNames != null) {
				List<String> exceptFileNameList = Arrays
						.asList(exceptFileNames);
				if (exceptFileNameList.contains(zipEntryName)) {
					continue;
				}
			}
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
			
				continue;
			}
			switch (zipMode) {
			case UPDATE:
				if (new File(outPath).exists()) {
					continue;
				}
				break;

			case COVER:

				break;
			}
			// 输出文件路径信息
			// System.out.println(outPath);
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[4096];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
			in = null;
			out = null;
		}
		System.out.println("******************解压完毕********************");
		return true;

	}

	/**
	 * 解压指定文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @param assignFileNames
	 *            解压的指定文件名称
	 * @author isea533
	 */
	public synchronized static boolean unZipAssignFiles(File zipFile, String descDir, String... assignFileNames) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		if(!descDir.endsWith(File.separator)){
			descDir=descDir+File.separator;
		}
		ZipFile zip = new ZipFile(zipFile.getPath(), "gbk");
		List<String> assignFileNameList =null;
		//待解压文件
		if (assignFileNames != null) {
			assignFileNameList = Arrays.asList(assignFileNames);
		}
		if(assignFileNameList==null||assignFileNameList.isEmpty()){
			return false;
		}
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			for(int i=0;i<assignFileNameList.size();i++) {
				String assignFileName=assignFileNameList.get(i);
				if(!assignFileName.equals(zipEntryName)&&!zipEntryName.contains(assignFileName)){
					continue;
				}
				InputStream in = zip.getInputStream(entry);
				String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
				
					continue;
				}
				// 输出文件路径信息
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[4096];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
				in = null;
				out = null;
			}
		}
		System.out.println("******************解压完毕********************");
		return true;

	}
	
	public static final String UTF8_BOM = "\uFEFF";

	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}
	// public static void main(String[] args) throws IOException {
	// /**
	// * 压缩文件
	// */
	// File[] files = new File[]{new File("C:/Users/win7/Desktop/Drug.java"),new
	// File("C:/Users/win7/Desktop/ceshi.db3"),new
	// File("C:/Users/win7/Desktop/netTest/netTest/AndroidManifest.xml")};
	// File zip = new File("d:/压缩.zip");
	// ZipFiles(zip,"3",files);

	// /**
	// * 解压文件
	// */
	// File zipFile = new File("/mnt/sdcard/cm7_ctwoz_adw.zip");
	// String path = "/mnt/sdcard/cm7_ctwoz_adw";
	// unZipFiles(zipFile.getPath(), path);
	// }
	
	/**
	 * 解压指定文件到指定目录
	 * 
	 * @param zipFile
	 * @param assignFileName
	 *            解压的指定文件名称
	 */
	public synchronized static InputStream unZipAssignFile(File zipFile, String assignFileName) throws IOException {
		ZipFile zip = new ZipFile(zipFile.getPath(), "gbk");
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			if(!assignFileName.equals(zipEntryName)){
				continue;
			}
			return  zip.getInputStream(entry);
		}
		return null;
	}
	
}
