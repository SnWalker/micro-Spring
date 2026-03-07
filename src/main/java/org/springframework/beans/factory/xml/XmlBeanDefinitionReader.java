package org.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException ex) {
            throw new BeansException("IOException parsing XML document from " + resource, ex);
        }
    }

    /**
     * DOM 文档对象解析标准流程
     * 解析 XML 输入流，提取并注册完整的 BeanDefinition。
     * 核心流程：读取 XML -> 遍历 <bean> 标签 -> 提取类信息与属性 -> 组装 BeanDefinition -> 注册到容器
     *
     * @param inputStream XML 文件的基础输入流
     */
    protected void doLoadBeanDefinitions(InputStream inputStream) {
        // 1. 将输入流加载为 DOM 文档树，并获取根节点 (通常是 <beans>)
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        // 2. 遍历根节点下的所有直接子节点
        for (int i = 0; i < childNodes.getLength(); i++) {
            // 过滤掉空白换行等 TextNode，只处理真正的 XML 元素节点
            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }

            Element bean = (Element) childNodes.item(i);
            // 3. 核心分支：只处理标签名为 <bean> 的元素
            if (BEAN_ELEMENT.equals(bean.getNodeName())) {

                // 3.1 提取 <bean> 标签的基础元数据
                String id = bean.getAttribute(ID_ATTRIBUTE);
                String name = bean.getAttribute(NAME_ATTRIBUTE);
                String className = bean.getAttribute(CLASS_ATTRIBUTE);

                // 3.2 核心反射：根据全限定类名，将 String 转化为 Java Class 对象
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new BeansException("Cannot find class [" + className + "]");
                }

                // 3.3 确定 Bean 的唯一名称 (优先级: id > name > 类名首字母小写)
                String beanName = StrUtil.isNotEmpty(id) ? id : name;
                if (StrUtil.isEmpty(beanName)) {
                    beanName = StrUtil.lowerFirst(clazz.getSimpleName());
                }

                // 3.4 实例化 BeanDefinition (注意：这里只是创建了图纸，还没有真正 new 出 Bean 对象)
                BeanDefinition beanDefinition = new BeanDefinition(clazz);

                // 4. 遍历当前 <bean> 标签下的子节点，寻找依赖注入的 <property>
                for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                    if (!(bean.getChildNodes().item(j) instanceof Element)) {
                        continue;
                    }

                    Element property = (Element) bean.getChildNodes().item(j);
                    if (PROPERTY_ELEMENT.equals(property.getNodeName())) {

                        // 4.1 提取属性名称、普通值 (value) 和 引用对象 (ref)
                        String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
                        String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
                        String refAttribute = property.getAttribute(REF_ATTRIBUTE);

                        if (StrUtil.isEmpty(nameAttribute)) {
                            throw new BeansException("The name attribute cannot be null or empty");
                        }

                        // 4.2 判断是普通数值还是对其他 Bean 的引用
                        Object value = valueAttribute;
                        if (StrUtil.isNotEmpty(refAttribute)) {
                            // 如果是 ref，则包装为内部的引用对象，留待后续实例化时解析
                            value = new BeanReference(refAttribute);
                        }

                        // 4.3 将解析好的属性放入 BeanDefinition 的属性集合中
                        PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }

                // 5. 校验重名并最终注册到容器 (缓存到 ConcurrentHashMap 中)
                if (getRegistry().containsBeanDefinition(beanName)) {
                    throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
                }
                getRegistry().registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }
}
