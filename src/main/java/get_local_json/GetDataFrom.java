package get_local_json;

import model.MdBean;
import tools.StaticUtilTool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import static tools.Tools.do_write_json_to_file;

public class GetDataFrom {

    public static void main(String[] args) throws IOException {

        LinkedList<String> list = new LinkedList<String>();
        HashMap<String, LinkedList<MdBean>> hashMap = new HashMap<String, LinkedList<MdBean>>();

        String prefix_str = "https://raw.githubusercontent.com/littleostar-blog/littleostar-blog/master/contents/";

        String root_path = "C:" + File.separator + "WebstormProjects";
        String path = new StaticUtilTool().do_find_match_single_endWith(root_path, "littleostar-blog");

        deal_list_map(list, hashMap, prefix_str, path + File.separator + "contents");

//        testJson(hashMap);
        do_write_json_to_file(list, hashMap);

    }

    private static void deal_list_map(
            LinkedList<String> list, HashMap<String, LinkedList<MdBean>> hashMap,
            String prefix_str, String file_path) {

        File rootFile = new File(file_path);
        File[] files = rootFile.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();

                list.add(fileName); // list add fileName
//                    System.out.println(fileName);

                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    hashMap.put(fileName, new LinkedList<MdBean>()); // hashmap put
                    for (File subFile : subFiles) {
                        String subFileName = subFile.getName();
                        String md_title = getReplaceEndMd(subFileName);
                        String md_url = prefix_str + fileName + "/" + subFileName;

                        hashMap.get(fileName).add(new MdBean(md_title, md_url)); // hashmap get && add
//                            System.out.println("    " + subFileName);
                    }
                }

            }
        }
    }

    private static void testJson(HashMap<String, LinkedList<MdBean>> hashMap) {
//        String json_map = gson.toJson(hashMap, new TypeToken<HashMap<String, LinkedList<MdBean>>>() {
//        }.getType());
//        System.out.println(json_map);
//        System.out.println("==========================");

        Set<String> keySet = hashMap.keySet();
        for (String key : keySet) {
            LinkedList<MdBean> list = hashMap.get(key);
            for (MdBean mdBean : list) {
                String md_title = mdBean.getMd_title();
                System.out.println(key + "/" + md_title);
            }
        }

    }

    private static String getReplaceEndMd(String subFileName) {
        return subFileName.replace(".md", "");
    }


}
