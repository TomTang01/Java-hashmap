import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystem {

    MyHashMap<String, ArrayList<FileData>> nameMap;
    MyHashMap<String, ArrayList<FileData>> dateMap;

    public FileSystem() {
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
    }

    public FileSystem(String inputFile) {
        // Add your code here
        nameMap = new MyHashMap<>();
        dateMap = new MyHashMap<>();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                // Add your code here
                add(data[0],data[1],data[2]);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public boolean add(String fileName, String directory, String modifiedDate) {
        if(fileName == null) {fileName = "";}
        if(directory == null) {directory = "/";}
        if(modifiedDate == null) {modifiedDate = "01/01/2021";}
        ArrayList<FileData> list = new ArrayList<FileData>();
        FileData newFile = new FileData(fileName,directory,modifiedDate);
        list.add(newFile);
        if(nameMap.containsKey(fileName)) {
            for(int i=0;i<nameMap.get(fileName).size();i++){
                if(nameMap.get(fileName).get(i).dir.equals(directory)){
                    return false;
                }
            }
            nameMap.get(fileName).add(newFile);
            if(dateMap.containsKey(modifiedDate)) {
                dateMap.get(modifiedDate).add(newFile);
                return true;
            }
            dateMap.put(modifiedDate,list);
            return true;
        }
        nameMap.put(fileName, list);
        if(dateMap.containsKey(modifiedDate)) {
            dateMap.get(modifiedDate).add(newFile);
            return true;
        }
        dateMap.put(modifiedDate,list);
        return true;
    }

    public FileData findFile(String name, String directory) {
        if(nameMap.containsKey(name)){
            for(int i=0;i<nameMap.get(name).size();i++){
                if(nameMap.get(name).get(i).dir.equals(directory)){
                    return nameMap.get(name).get(i);
                }
            }
        }
        return null;
    }

    public ArrayList<String> findAllFilesName() {
        return (ArrayList<String>) nameMap.keys();
    }

    public ArrayList<FileData> findFilesByName(String name) {
        if(nameMap.get(name) == null) {return new ArrayList<FileData>();}
        return nameMap.get(name);
    }

    public ArrayList<FileData> findFilesByDate(String modifiedDate) {
        if(dateMap.get(modifiedDate) == null) {return new ArrayList<FileData>();}
        return dateMap.get(modifiedDate);
    }

    public ArrayList<FileData> findFilesInMultDir(String modifiedDate) {
        ArrayList<FileData> filesByDate = dateMap.get(modifiedDate);
        if(filesByDate.size() == 0) {return filesByDate = new ArrayList<FileData>();}
        boolean hasSame = false;
        for(int i=0;i<filesByDate.size()-1;i++){
            for(int j=i+1;i<filesByDate.size();j++){
                if(filesByDate.get(i).name.equals(filesByDate.get(j).name)){
                    filesByDate.remove(j);
                    hasSame = true;
                }
            }
            if(!hasSame) {filesByDate.remove(i);}
        }
        return filesByDate;
    }

    public boolean removeByName(String name) {
        if(nameMap.remove(name)) {
            ArrayList<String> list = (ArrayList<String>) dateMap.keys();
            for(int i=0;i<list.size();i++){
                for(int j=0;j<dateMap.get(list.get(i)).size();j++){
                    if(dateMap.get(list.get(i)).get(j).name.equals(name)){
                        dateMap.get(list.get(i)).remove(j);
                    }
                }
                if(dateMap.get(list.get(i)).size() == 0) {dateMap.remove(list.get(i));}
            }
            return true;
        }
        return false;
    }

    public boolean removeFile(String name, String directory) {
        boolean isRemoved = false;
        if(nameMap.containsKey(name)) {
            for(int i=0;i<nameMap.get(name).size();i++){
                if(nameMap.get(name).get(i).dir.equals(directory)){
                    nameMap.get(name).remove(i);
                    isRemoved = true;
                    break;
                }
            }
            if(nameMap.get(name).size()==0) {nameMap.remove(name);}
            ArrayList<String> list = (ArrayList<String>) dateMap.keys();
            for(int i=0;i<list.size();i++){
                for(int j=0;j<dateMap.get(list.get(i)).size();j++){
                    if(dateMap.get(list.get(i)).get(j).name.equals(name)
                            && dateMap.get(list.get(i)).get(j).dir.equals(directory)){
                        dateMap.get(list.get(i)).remove(j);
                    }
                }
                if(dateMap.get(list.get(i)).size() == 0) {dateMap.remove(list.get(i));}
            }
            return isRemoved;
        }
        return false;
    }

}
