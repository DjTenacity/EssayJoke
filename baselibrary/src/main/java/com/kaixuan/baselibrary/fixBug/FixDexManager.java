package com.kaixuan.baselibrary.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Administrator on 2017/7/28.
 */

public class FixDexManager {
    Context mContext;
    private File mDexDir;


    //防止内存泄漏，不单利by
    public FixDexManager(Context context) {
        this.mContext = context;
        //获取应用可以访问的dex目录
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 下载的修复dex包
     */
    public void fixDex(String fixDexPath) throws Exception {


        //2.获取下载好的补丁dexElement   mDexDir
        //2.1移动到系统能够访问的dex目录下 classLoader
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }

        //拷贝到
        File desFile = new File(mDexDir, srcFile.getName());

        if (desFile.exists()) {

            Log.d("FixPathManager", "patch{" + fixDexPath + "} path hsa be loaded");
            return;//celass会附带版本号，几乎不会有同名的情况，所以不用考虑覆盖
        }
        copyFile(srcFile, desFile);

        //2.2classLoader读取fixDex路径   加入集合的原因 已启动就要修复baseApplication
        List<File> fixDexFiles = new ArrayList<File>();

        fixDexFiles.add(desFile);

        fixDexFiles(fixDexFiles);

    }

    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }


    /**
     * 从classLoader中获取 dexElements
     */
    private Object getDesElenmentsByClassLoader(ClassLoader classLoder) throws Exception {
        //先获取pathList ，通过反射
        Field pathListFiele = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListFiele.setAccessible(true);
        Object pathList = pathListFiele.get(classLoder);

        //pathList里面的dexElements
        Field dexElementsFiled = pathList.getClass().getDeclaredField("dexElements");
        dexElementsFiled.setAccessible(true);

        return dexElementsFiled.get(pathList);
    }


    /**
     * 把dexElement注入到已运行classLoader中
     *
     * @param classLoader
     * @param dexElement
     * @throws Exception
     */
    private static void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception {
        Class<?> classLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        //先获取pathList
        Field pathListField = classLoaderClass.getDeclaredField("pathList");
        //反射
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        Class<?> pathListClass = pathList.getClass();
        Field dexElementsField = pathListClass.getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElement);
    }

    /**
     * 合并两个dexElements数组
     *
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    //加载之前的 所有修复的 apatch 包
    public void loadFixDex() throws Exception {
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();

        for (File dexFile : dexFiles) {

            if (dexFile.getName().endsWith(".dex")) {

                fixDexFiles.add(dexFile);
            }

        }
        fixDexFiles(fixDexFiles);
    }

    //修复dex
    private void fixDexFiles(List<File> fixDexFiles) throws Exception {

        //1.先获取已经运行DexElement
        ClassLoader applicationClassLoder = mContext.getClassLoader();
        Object applicationDexElements = getDesElenmentsByClassLoader(applicationClassLoder);

        File optimizedDirectory = new File(mDexDir, "odex");

        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();//创建
        }

        //修复
        for (File fixDexFile : fixDexFiles) {

            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    // dexPath dex路径  必须要在应用目录下的dex文件中
                    //解压路径
                    //so文件的位置
                    // 父的classLoader
                    fixDexFile.getAbsolutePath(),
                    optimizedDirectory,
                    null, applicationClassLoder
            );
            Object fixDexElements = getDesElenmentsByClassLoader(fixDexClassLoader);
            //把补丁的dexElement 插到已经运行的dexElement的最前面，合并

            // applicationClassLoader 数组合并，fixDexElements 也是数组

            //3,把补丁的dexElement插到已经运行的dexElement的最前面

            //合并完成，然后需要做的就是注入
            applicationDexElements = combineArray(fixDexClassLoader, applicationClassLoder);
        }
        //把合并的数组注入到注入到原来的类中  applicationClassLoder
        injectDexElements(applicationClassLoder, applicationDexElements);
    }
}
