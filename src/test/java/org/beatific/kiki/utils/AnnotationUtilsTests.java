package org.beatific.kiki.utils;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationUtilsTests {

	@Test
	public void testFindClassByAnnotation() {
		List<Class<?>> list = AnnotationUtils.findClassByAnnotation("org.beatific", TestAnnotation.class);
		assertEquals(AnnotationUtilsTestObject.class, list.get(0));
	}
}
