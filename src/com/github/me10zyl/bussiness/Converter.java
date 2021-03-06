package com.github.me10zyl.bussiness;

import com.github.me10zyl.entity.ClassProperty;

import java.util.List;

/**
 * Created by ZyL on 2017/1/23.
 */
public class Converter {
    
    private List<ClassProperty> source;
    private List<ClassProperty> target;

    public Converter(List<ClassProperty> props1, List<ClassProperty> props2) {
        this.target = props2;
        this.source = props1;
    }

    private int getSizeOfProp(List<ClassProperty> list){
        int size = 0;
        for(ClassProperty item : list){
            if(!item.isPlaceHolder()){
                size++;
            }
        }
        return size;
    }

    public String generate(){
        StringBuilder sb = new StringBuilder();
        if(getSizeOfProp(target) > 0 && getSizeOfProp(source) > 0){
            String targetClass = target.get(0).getPsiClass().getName();
            String sourceClass = source.get(0).getPsiClass().getName();
            sb.append(String.format("public %s convert(%s source){\n\t%s target = new %s();\n", targetClass, sourceClass, targetClass, targetClass));
            for(int i = 0; i < target.size(); i++){
                ClassProperty targetProp = target.get(i);
                if(!targetProp.isEnable() || targetProp.isPlaceHolder()){
                    continue;
                }
                ClassProperty sourceProp = null;
                if(i < source.size()){
                    sourceProp = source.get(i);
                }
                if(sourceProp.isPlaceHolder()){
                    sb.append(String.format("\ttarget.set%s(null);\n", targetProp.getNameUpperFirst()));
                }else{
                    sb.append(String.format("\ttarget.set%s(source.get%s());\n", targetProp.getNameUpperFirst(), sourceProp.getNameUpperFirst()));
                }

            }
            sb.append(String.format("\treturn target;\n}")) ;
        }
        return sb.toString();
    }
}
