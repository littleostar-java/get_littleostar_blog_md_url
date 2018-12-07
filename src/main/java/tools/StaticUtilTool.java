package tools;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class StaticUtilTool {

    // del name
    private String default_delete_name = "node_modules";
    private FilenameFilter default_do_delete_name_filter = (dir, name) ->
            name.contains(default_delete_name) && name.lastIndexOf(default_delete_name) == 0;
    private String default_match_name = ".md";
    private FilenameFilter default_do_rename_name_filter = (dir, name) -> name.endsWith(default_match_name);

    /**
     * print the file name
     *
     * @param file
     */
    private void print_path(File file) {
//        System.out.println("deleted: " + file.getAbsolutePath());
    }

    /**
     * @param root_path where do you want to find node_modules , then delete it
     * @throws IOException
     */
    public void do_delete_by_path(String root_path) throws IOException {
        do_delete_by_path(root_path, "");
    }

    /**
     * @param root_path where do you want to find node_modules , then delete it
     * @param del_name  you want to delete file name
     * @throws IOException
     */
    public void do_delete_by_path(String root_path, String del_name) throws IOException {
        LinkedList<String> list = new LinkedList<String>();

        File rootDir = new File(root_path); // WebstormProjects root dir
        if (!rootDir.exists()) {
            System.out.println("not exists");
            System.exit(0);
        } else {
            LinkedList<String> pathList = new LinkedList<>();
            if (Objects.requireNonNull(del_name).length() == 0) {
                pathList = get_path_list_by_root(rootDir, list, default_do_delete_name_filter);
            } else {
                FilenameFilter your_filter = (dir, name) ->
                        name.contains(del_name) && name.lastIndexOf(del_name) == 0;
                pathList = get_path_list_by_root(rootDir, list, your_filter);
            }
            for (String str : pathList) {
                delete_file(str);
                System.out.println("deleted:\n\t\t" + str);
            }
        }
    }

    public void do_rename_by_path(String root_path, String old_char, String new_char) throws IOException {
        do_rename_by_path(root_path, "", old_char, new_char);
    }

    public void do_rename_by_path(String root_path, String match_name, String old_char, String new_char)
            throws IOException {
        LinkedList<String> list = new LinkedList<String>();

        if (match_name.length() == 0) {
            get_path_list_by_root(root_path, list, default_do_rename_name_filter);
        } else {
            FilenameFilter new_name_filter = (dir, name) -> name.endsWith(match_name);
            get_path_list_by_root(root_path, list, new_name_filter);
        }

        LinkedList<String> new_list = new LinkedList<String>();
//        System.out.println("matched size()=" + list.size());
        for (String str : list) {
            File file = new File(str);
            String fileName = file.getName();

            boolean contains = fileName.contains(old_char);
            if (contains) {
                new_list.add(str);
            }
        }

        System.out.println("new_list.size()=" + new_list.size());
        for (String str : new_list) {
            System.out.println(str);
        }

        if (new_list.size() > 0) {
            for (String str : new_list) {
                File old_file = new File(str);
                String prefix_str = old_file.getAbsolutePath().substring(0, old_file.getAbsolutePath().indexOf(old_file.getName()));
                String end_str = old_file.getName().replace(old_char, new_char);
                String new_path_name = prefix_str + end_str;
                System.out.println(new_path_name);

                File new_file = new File(new_path_name);

                boolean b = old_file.renameTo(new_file);
                if (b) {
                    System.out.println("success:\n\t\t" + new_file.getAbsolutePath());
                } else {
                    System.out.println("failed:\n\t\t" + old_file.getAbsolutePath());
                }

            }
        }
    }

    /**
     * @param root_path
     * @param list
     * @param filenameFilter
     * @return
     */
    public LinkedList<String> get_path_list_by_root(
            String root_path, LinkedList<String> list, FilenameFilter filenameFilter)
            throws IOException {
        return get_path_list_by_root(new File(root_path), list, filenameFilter);
    }

    /**
     * @param root_path
     * @param match_name
     * @return
     * @throws IOException
     */
    public String do_find_match_single_endWith(String root_path, String match_name) throws IOException {
        String path = null;

        LinkedList<String> list = new LinkedList<String>();
        FilenameFilter filenameFilter = (file, name) -> name.endsWith(match_name);
        get_path_list_by_root(root_path, list, filenameFilter);

        if (list.size() != 0) {
            String str = list.get(0);
            File file = new File(str);
            path = file.getAbsolutePath();
        }

        return path;
    }

    /**
     * @param root_file
     * @param list           container "node_modules" of list
     * @param filenameFilter
     * @return
     * @throws IOException
     */
    public LinkedList<String> get_path_list_by_root(
            File root_file, LinkedList<String> list, FilenameFilter filenameFilter)
            throws IOException {
        if (root_file.isDirectory() && Objects.requireNonNull(root_file.list()).length != 0) {
            File[] files_havefilter = root_file.listFiles(filenameFilter);
            File[] files_nofilter = root_file.listFiles();

            assert files_nofilter != null;
            if (files_nofilter.length != 0) {
                for (File file_no : files_nofilter) {
                    assert files_havefilter != null;
                    if (files_havefilter.length == 0) {
                        get_path_list_by_root(file_no, list, filenameFilter);
                    }
                }
                for (File file_have : files_havefilter) {
                    String absolutePath = file_have.getAbsolutePath();
                    list.add(absolutePath);
                }
            }
        }
        return list;
    }

    /**
     * delete file by path
     *
     * @param path absolute path of file
     * @throws IOException
     */
    private void delete_file(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] list = file.list();
            assert list != null;
            if (list.length == 0) {
                file.delete();
                print_path(file);
            } else {
                for (String temp : list) {
                    File fileDel = new File(file, temp);
                    delete_file(fileDel.getAbsolutePath());
                }
                if (file.list().length == 0) {
                    file.delete();
                    print_path(file);
                }
            }
        } else {
            file.delete();
            print_path(file);
        }
    }

}
