package org.beatific.kiki.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class AnnotationUtils {

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
	
	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(basePackage);
	}
	
	private static Class<?> getClass(MetadataReader metadataReader) throws ClassNotFoundException, LinkageError {
		return ClassUtils.forName(metadataReader.getClassMetadata().getClassName(), ClassUtils.getDefaultClassLoader());
	}
	
	private static boolean isClassWithAnnotation(Class<?> clazz, Class annotation) {
		return clazz.getAnnotation(annotation) != null;
	}
	
	public static List<Class<?>> findClassByAnnotation(String basePackage, Class<?> annotationType) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
						Class<?> clazz = getClass(metadataReader);
						if (isClassWithAnnotation(clazz, annotationType)) {
							classes.add(clazz);
						}
					}catch (Throwable ex) {
						throw new BeanDefinitionStoreException(
								"Failed to read class: " + resource, ex);
					}
				}
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		}
		return classes;
	}
	
	
}
