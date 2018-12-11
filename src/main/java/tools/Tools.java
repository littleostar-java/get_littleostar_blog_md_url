package tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.MdBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Tools {
    private static Gson gson = new GsonBuilder().create();

    public static void do_write_json_to_dir_file(LinkedList<String> list, HashMap<String, LinkedList<MdBean>> hashMap) {
        String json_array = gson.toJson(list);
        String json_map = gson.toJson(hashMap, new TypeToken<HashMap<String, LinkedList<MdBean>>>() {
        }.getType());

        String path_parent = System.getProperty("user.dir") + File.separator + "json_";
        String sub_path = path_parent + File.separator + "md_array";

        write_text_to_file(path_parent, null, "md_array.json", json_array);
//        write_text_to_file(path_parent, null, "md_map.json", json_map);

        Set<String> set = hashMap.keySet();
        for (String key : set) {
            LinkedList<MdBean> linkedList = hashMap.get(key);
            write_text_to_file(path_parent, sub_path, key + ".json", gson.toJson(linkedList));
        }

    }

    private static void write_text_to_file(String path_parent, String sub_path, String write_to_fileName, String text) {
        try {
            File file_parent = null;
            file_parent = new File(path_parent);
            if (sub_path != null) {
                file_parent = new File(sub_path);
            }
            if (!file_parent.exists()) {
                boolean mkdir = file_parent.mkdir();
                if (!mkdir) {
                    return;
                }
            }

            File file = new File(file_parent, write_to_fileName);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    return;
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(text);
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(write_to_fileName + "write success ... ");
        }
    }

}
