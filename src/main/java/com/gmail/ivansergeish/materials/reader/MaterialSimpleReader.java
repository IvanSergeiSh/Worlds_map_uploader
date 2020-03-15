package com.gmail.ivansergeish.materials.reader;

import java.io.File;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gmail.ivansergeish.service.ObjectDBService;
import com.gmail.ivansergeish.utils.WaveFrontUtils;

@Service
public class MaterialSimpleReader implements MaterialsReader {
	private static final String SPRITE = "sprite";
	//https://10.42.0.1:8443/api-launcher-1.0.0.alpha-RELEASE/map/sprite.jpg
	@Autowired
	private Environment env;

	@Autowired
	private ObjectDBService objectService;
	
	@Autowired
	private WaveFrontUtils utils;
	
	@Override
	public byte[] read(String fName) {
		try {
			return readMaterials(fName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO add new line symbol
	
   byte[] readMaterials(String fName) throws IOException{
		
		File file = new File(fName);
		List<String> list = Files.readAllLines(file.toPath());
		
		for(int i = 0; i < list.size(); i++) {
			String[] strs = list.get(i).split(" ");
			if (strs.length > 0 && strs[0].equals("newmtl")) {
				String newMtl = "newmtl " + strs[1].replaceAll(".jpg", "");
				list.add(i, newMtl);
				list.remove(i + 1);
				//newMtl = list.get(i).replaceAll(".", "_");
				//list.add(i, newMtl);
				//list.remove(i + 1);		
			}
			if (strs.length > 0 && strs[0].equals("map_Kd")) {
				//String newMapKd = "map_Kd " + pathToUrl(strs[1].replaceAll(".jpg", ""));//strs[1].replaceAll(".jpg", "");
				String newMapKd = list.get(i).replaceAll(".jpg", "");
				newMapKd = "map_Kd " + pathToUrl(newMapKd.replaceAll("map_Kd ", ""));
				list.add(i, newMapKd);
				list.remove(i + 1);
			}
			
			String str = list.get(i) + "\n";
			list.add(i, str);
			list.remove(i + 1);
		}
		
		String folder = getFolder(fName);
		List<String> spritesList = getSpritesList(list);
		try {
			uploadSprites(spritesList, folder);
		} catch(IOException e) {}

		return toByteArray(list);
	}
   
   void uploadSprites(List<String> spritesList, String folder) throws IOException {
		for (int i = 0; i < spritesList.size(); i++) {
			String str = folder + getName(spritesList.get(i));
			byte[] sprite = utils.readFileToByteArray(str.replaceAll("\n", "") + ".jpg");
			objectService.saveSprite(sprite, getName(spritesList.get(i)).replaceAll("\n", ""));
	
		}
   }
   String getName(String url) {
	   String separator = getPathSeparator(url);
	   String[] fStrs = url.split(separator);
	   return fStrs[fStrs.length-1]; 
   }
   //TODO currebt implementation is only for linux
	String getFolder(String fName) {
		//File file = new File(fName);
		String separator = getPathSeparator(fName);
		String[] fStrs = fName.split(separator);//(file.pathSeparator);
		String folder = "";
		int k = separator.equals("\\") ? 0 : 1; //windows : 0; linux : 1
		for (int i = k; i < fStrs.length - 1; i++) {
			folder = folder + separator + fStrs[i];
		}
		return folder + separator;
	}
	/**
	 * 
	 * @param path
	 * map_Kd C:\Users\С.А. Шишкин\Pictures\moscow\srub4-1.jpg
	 * @return
	 */
	String getPathSeparator(String path) {
		if (path.contains(":\\\\")) { return "\\\\";}
		return path.contains(":\\") ? "\\\\" : "/";
	}
	
	String pathToUrl(String path) {
		String url = env.getProperty("server.url");
		String spriteName;
		String regex = getPathSeparator(path);//path.contains(":\\\\") ? "\\\\" : "/"; 
		String[] strs = path.split(regex);
		spriteName = strs[strs.length - 1];
		url = url.concat(SPRITE).concat("/").concat(spriteName);
		return url;
	}
	
	String[] split(String path) {
		Pattern  p = Pattern.compile("\\", Pattern.CASE_INSENSITIVE);
		return p.split(path);
	}
	/**
	 *Ggenerate's list of sprite names to be uploaded
	 * @param mtlFileStrings
	 * @return
	 */
	List<String> getSpritesList(List<String> mtlFileStrings) {
		List<String> result = new ArrayList(mtlFileStrings.size()/5);
		for (int i = 0; i < mtlFileStrings.size(); i++) {
		    if (mtlFileStrings.get(i).contains("map_Kd")) { 
				String newMtl = mtlFileStrings.get(i).replaceAll("map_Kd ", "")
						.replaceAll(".jpg", "");
				result.add(newMtl);
		    }
		}
		return result;		
	}
	
	private byte[] toByteArray(List<String> list) {
		int bytesAmount = 0;
		for (int i = 0; i < list.size(); i++) {
			bytesAmount = bytesAmount + list.get(i).getBytes().length;
		}		
		byte[] result = new byte[bytesAmount];
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			byte[] bytesToCopy = list.get(i).getBytes();
			System.arraycopy(bytesToCopy, 0, result, index, bytesToCopy.length);
			index = index + bytesToCopy.length;
		}
		return result;
	}
}
