package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.util.Log;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TestConnect {
    TagNode rootNode;

    public TestConnect(URL url) {

        HtmlCleaner cleaner = new HtmlCleaner();

        try {
            rootNode = cleaner.clean(url);
        } catch (IOException e) {

            Log.e("Look", "Error IOException -- TestConnect - TestConnect(Url url) - " + e.getMessage());
            e.printStackTrace();
        }

    }

    List<TagNode> getLinksByClass(String CSSClassname) {
        List<TagNode> linkList = new ArrayList<TagNode>();

        //Выбираем все ссылки
        TagNode linkElements[] = rootNode.getElementsByName("a", true);
        for (int i = 0; linkElements != null && i < linkElements.length; i++) {
            //получаем атрибут по имени
            String classType = linkElements[i].getAttributeByName("class");
            //если атрибут есть и он эквивалентен искомому, то добавляем в список
            if (classType != null && classType.equals(CSSClassname)) {
                linkList.add(linkElements[i]);
            }
        }

        return linkList;
    }
}
