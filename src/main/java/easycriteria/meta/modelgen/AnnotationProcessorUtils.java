package easycriteria.meta.modelgen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * Series of method to aid in the writing of annotation processors.
 */
public class AnnotationProcessorUtils {
	private static Set<String> LIST_CLASSNAMES = null;
	private static Set<String> SET_CLASSNAMES = null;
	private static Set<String> MAP_CLASSNAMES = null;
	private static Set<String> COLLECTION_CLASSNAMES = null;
	private static Set<String> STRING_CLASSNAMES = null;
	private static Set<String> NUMBER_CLASSNAMES = null;
	private static Set<String> DATE_CLASSNAMES = null;
	static {
		LIST_CLASSNAMES = new HashSet<String>();
		LIST_CLASSNAMES.add("java.util.List");
		LIST_CLASSNAMES.add("java.util.ArrayList");
		LIST_CLASSNAMES.add("java.util.AbstractList");
		LIST_CLASSNAMES.add("java.util.Stack");
		LIST_CLASSNAMES.add("java.util.Vector");
		LIST_CLASSNAMES.add("java.util.LinkedList");
		SET_CLASSNAMES = new HashSet<String>();
		SET_CLASSNAMES.add("java.util.Set");
		SET_CLASSNAMES.add("java.util.HashSet");
		SET_CLASSNAMES.add("java.util.AbstractSet");
		SET_CLASSNAMES.add("java.util.LinkedHashSet");
		SET_CLASSNAMES.add("java.util.TreeSet");
		SET_CLASSNAMES.add("java.util.SortedSet");
		MAP_CLASSNAMES = new HashSet<String>();
		MAP_CLASSNAMES.add("java.util.Map");
		MAP_CLASSNAMES.add("java.util.HashMap");
		MAP_CLASSNAMES.add("java.util.AbstractMap");
		MAP_CLASSNAMES.add("java.util.Hashtable");
		MAP_CLASSNAMES.add("java.util.LinkedHashMap");
		MAP_CLASSNAMES.add("java.util.TreeMap");
		MAP_CLASSNAMES.add("java.util.SortedMap");
		MAP_CLASSNAMES.add("java.util.Properties");
		COLLECTION_CLASSNAMES = new HashSet<String>();
		COLLECTION_CLASSNAMES.add("java.util.Collection");
		COLLECTION_CLASSNAMES.add("java.util.AbstractCollection");
		COLLECTION_CLASSNAMES.add("java.util.Queue");
		COLLECTION_CLASSNAMES.add("java.util.PriorityQueue");
		STRING_CLASSNAMES = new HashSet<String>();
		STRING_CLASSNAMES.add("java.lang.String");
		NUMBER_CLASSNAMES = new HashSet<String>();
		NUMBER_CLASSNAMES.add("java.lang.Integer");
		NUMBER_CLASSNAMES.add("java.lang.Double");
		NUMBER_CLASSNAMES.add("java.lang.Long");
		NUMBER_CLASSNAMES.add("java.lang.Number");
		NUMBER_CLASSNAMES.add("java.lang.Float");
		NUMBER_CLASSNAMES.add("java.math.BigDecimal");
		DATE_CLASSNAMES = new HashSet<String>();
		DATE_CLASSNAMES.add("java.time.ZonedDateTime");
		DATE_CLASSNAMES.add("java.time.LocalDateTime");
		DATE_CLASSNAMES.add("java.time.LocalDateTime");
		DATE_CLASSNAMES.add("java.time.LocalDate");
		DATE_CLASSNAMES.add("java.time.LocalTime");
	}

	public static enum TypeCategory {
		
		COLLECTION("CollectionAttribute"),
		SET("CollectionAttribute"),
		LIST("CollectionAttribute"),
		MAP("MapAttribute"),
		ATTRIBUTE("PropertyAttribute"), 
		STRING_ATTRIBUTE("StringPropertyAttribute"), 
		OBJECT_ATTRIBUTE("ObjectAttribute"),
		JPA_ENTITY_ATTRIBUTE("ObjectAttribute"), 
		NUMBER_ATTRIBUTE("NumberPropertyAttribute"), 
		DATE_ATTRIBUTE("DatePropertyAttribute");

		private String type;

		private TypeCategory(String type) {
			this.type = type;
		}

		public String getTypeName() {
			return type;
		}
	}

	/**
	 * Method to return the type category for a type.
	 * 
	 * @param typeName
	 *            The type name (e.g java.lang.String, java.util.Collection)
	 * @return The type category
	 */
	public static TypeCategory getTypeCategoryForTypeMirror(String typeName) {
		if (COLLECTION_CLASSNAMES.contains(typeName)) {
			return TypeCategory.COLLECTION;
		} else if (SET_CLASSNAMES.contains(typeName)) {
			return TypeCategory.SET;
		} else if (LIST_CLASSNAMES.contains(typeName)) {
			return TypeCategory.LIST;
		} else if (MAP_CLASSNAMES.contains(typeName)) {
			return TypeCategory.MAP;
		} else if (STRING_CLASSNAMES.contains(typeName)) {
			return TypeCategory.STRING_ATTRIBUTE;
		} else if (NUMBER_CLASSNAMES.contains(typeName)) {
			return TypeCategory.NUMBER_ATTRIBUTE;
		} else if (DATE_CLASSNAMES.contains(typeName)) {
			return TypeCategory.DATE_ATTRIBUTE;
		}

		return TypeCategory.ATTRIBUTE;
	}

	public static boolean isFieldJPAAEntity(ProcessingEnvironment processingEnv, Element el) {	
		
		boolean jpaEntity = false;
		
		TypeMirror type = AnnotationProcessorUtils.getDeclaredType(el);		
		TypeElement typeElement = (TypeElement) processingEnv.getTypeUtils().asElement(type);
		if (typeElement != null) {
			if (isJPAAnnotated(typeElement)){
				jpaEntity = true;
			}
			if (getPersistentSupertype(processingEnv, typeElement) != null && isJPAAnnotated(getPersistentSupertype(processingEnv, typeElement))) {
				jpaEntity = true;
			}
		}

		return jpaEntity;
	}

	/**
	 * Convenience method to return if this class element has any of the
	 * defining JPA annotations.
	 * 
	 * @param el
	 *            The class element
	 * @return Whether it is to be considered a JPA annotated class
	 */
	public static boolean isJPAAnnotated(Element el) {
		if (el == null) {
			return false;
		}
		if ((el.getAnnotation(Entity.class) != null) || (el.getAnnotation(MappedSuperclass.class) != null)
				|| (el.getAnnotation(Embeddable.class) != null)) {
			return true;
		}
		return false;
	}

	/**
	 * Convenience accessor for all field members of the supplied type element.
	 * 
	 * @param el
	 *            The type element
	 * @return The field members
	 */
	public static List<? extends Element> getFieldMembers(TypeElement el) {
		List<? extends Element> members = el.getEnclosedElements();
		List<Element> fieldMembers = new ArrayList<Element>();
		Iterator<? extends Element> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			Element member = memberIter.next();
			if (member.getKind() == ElementKind.FIELD) {
				fieldMembers.add(member);
			}
		}
		return fieldMembers;
	}

	/**
	 * Convenience accessor for all property members of the supplied type
	 * element.
	 * 
	 * @param el
	 *            The type element
	 * @return The property members
	 */
	public static List<? extends Element> getPropertyMembers(TypeElement el) {
		List<? extends Element> members = el.getEnclosedElements();
		List<Element> propertyMembers = new ArrayList<Element>();
		Iterator<? extends Element> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			Element member = memberIter.next();
			if (member.getKind() == ElementKind.METHOD) {
				ExecutableElement method = (ExecutableElement) member;
				if (isJavaBeanGetter(method) || isJavaBeanSetter(method)) {
					propertyMembers.add(member);
				}
			}
		}
		return propertyMembers;
	}

	/**
	 * Convenience method to return if the provided method is a java bean
	 * getter.
	 * 
	 * @param method
	 *            The method
	 * @return Whether it is a java bean getter
	 */
	public static boolean isJavaBeanGetter(ExecutableElement method) {
		String methodName = method.getSimpleName().toString();
		if (method.getKind() == ElementKind.METHOD && method.getParameters().isEmpty()) {
			if (returnsBoolean(method) && methodName.startsWith("is")) {
				return true;
			} else if (methodName.startsWith("get") && !returnsVoid(method)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Accessor for the member name for a field or Java bean getter/setter. If
	 * this is a field then just returns the field name, and if
	 * "getXxx"/"setXxx" then returns "xxx"
	 * 
	 * @param el
	 *            The element
	 * @return The member name
	 */
	public static String getMemberName(Element el) {
		if (el.getKind() == ElementKind.FIELD) {
			return el.toString();
		} else if (el.getKind() == ElementKind.METHOD) {
			ExecutableElement method = (ExecutableElement) el;
			if (isJavaBeanGetter(method) || isJavaBeanSetter(method)) {
				String name = method.getSimpleName().toString();
				if (name.startsWith("is")) {
					name = name.substring(2);
				} else {
					name = name.substring(3);
				}
				return Character.toLowerCase(name.charAt(0)) + name.substring(1);
			}
		}
		return null;
	}

	/**
	 * Convenience method to return if the provided method is a java bean
	 * setter.
	 * 
	 * @param method
	 *            The method
	 * @return Whether it is a java bean setter
	 */
	public static boolean isJavaBeanSetter(ExecutableElement method) {
		String methodName = method.getSimpleName().toString();
		return method.getKind() == ElementKind.METHOD && methodName.startsWith("set")
				&& method.getParameters().isEmpty() && !returnsVoid(method);
	}

	/**
	 * Convenience method to return if the provided element represents a method
	 * (otherwise a field).
	 * 
	 * @param elem
	 *            The element
	 * @return Whether it represents a method
	 */
	public static boolean isMethod(Element elem) {
		return elem != null && ExecutableElement.class.isInstance(elem) && elem.getKind() == ElementKind.METHOD;
	}

	/**
	 * Accessor for the declared type of this element. If this is a field then
	 * returns the declared type of the field. If this is a java bean getter
	 * then returns the return type.
	 * 
	 * @param elem
	 *            The element
	 * @return The declared type
	 */
	public static TypeMirror getDeclaredType(Element elem) {
		if (elem.getKind() == ElementKind.FIELD) {
			return elem.asType();
		} else if (elem.getKind() == ElementKind.METHOD) {
			return ((ExecutableElement) elem).getReturnType();
		} else {
			throw new IllegalArgumentException("Unable to get type for " + elem);
		}
	}

	/**
	 * Accessor for the value for an annotation attribute.
	 * 
	 * @param elem
	 *            The element
	 * @param annotCls
	 *            Annotation class
	 * @param attribute
	 *            The attribute we're interested in
	 * @return The value
	 */
	@SuppressWarnings("rawtypes")
	public static Object getValueForAnnotationAttribute(Element elem, Class annotCls, String attribute) {
		List<? extends AnnotationMirror> anns = elem.getAnnotationMirrors();
		Iterator<? extends AnnotationMirror> annIter = anns.iterator();
		while (annIter.hasNext()) {
			AnnotationMirror ann = annIter.next();
			if (ann.getAnnotationType().toString().equals(annotCls.getName())) {
				Map<? extends ExecutableElement, ? extends AnnotationValue> values = ann.getElementValues();
				for (Map.Entry entry : values.entrySet()) {
					ExecutableElement ex = (ExecutableElement) entry.getKey();
					if (ex.getSimpleName().toString().equals(attribute)) {
						return ((AnnotationValue) entry.getValue()).getValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Convenience method returning if the provided method returns void.
	 * 
	 * @param method
	 *            The method
	 * @return Whether it returns void
	 */
	public static boolean returnsVoid(ExecutableElement method) {
		TypeMirror type = method.getReturnType();
		return (type != null && type.getKind() == TypeKind.VOID);
	}

	/**
	 * Convenience method returning if the provided method returns boolean.
	 * 
	 * @param method
	 *            The method
	 * @return Whether it returns boolean
	 */
	public static boolean returnsBoolean(ExecutableElement method) {
		TypeMirror type = method.getReturnType();
		return (type != null && (type.getKind() == TypeKind.BOOLEAN || "java.lang.Boolean".equals(type.toString())));
	}

	/**
	 * Convenience method to return if the provided type is a primitive.
	 * 
	 * @param type
	 *            The type
	 * @return Whether it is a primitive
	 */
	public static boolean typeIsPrimitive(TypeMirror type) {
		TypeKind kind = type.getKind();
		return kind == TypeKind.BOOLEAN || kind == TypeKind.BYTE || kind == TypeKind.CHAR || kind == TypeKind.DOUBLE
				|| kind == TypeKind.FLOAT || kind == TypeKind.INT || kind == TypeKind.LONG || kind == TypeKind.SHORT;
	}

	/**
	 * Method to return the declared type name of the provided TypeMirror.
	 * 
	 * @param processingEnv
	 *            Processing environment
	 * @param type
	 *            The type (mirror)
	 * @param box
	 *            Whether to (auto)box this type
	 * @return The type name (e.g "java.lang.String")
	 */
	public static String getDeclaredTypeName(ProcessingEnvironment processingEnv, TypeMirror type, boolean box) {
		if (type == null || type.getKind() == TypeKind.NULL || type.getKind() == TypeKind.WILDCARD) {
			return "java.lang.Object";
		}
		if (type.getKind() == TypeKind.ARRAY) {
			TypeMirror comp = ((ArrayType) type).getComponentType();
			return getDeclaredTypeName(processingEnv, comp, false);
		}

		if (box && AnnotationProcessorUtils.typeIsPrimitive(type)) {
			type = processingEnv.getTypeUtils().boxedClass((PrimitiveType) type).asType();
		}
		if (AnnotationProcessorUtils.typeIsPrimitive(type)) {
			return ((PrimitiveType) type).toString();
		}
		return processingEnv.getTypeUtils().asElement(type).toString();
	}

	/**
	 * Convenience accessor for members for the default access type of the
	 * supplied type element. If properties are annotated then returns all
	 * properties, otherwise returns all fields.
	 * 
	 * @param el
	 *            The type element
	 * @return The members
	 */
	public static List<? extends Element> getDefaultAccessMembers(TypeElement el) {
		Iterator<? extends Element> memberIter = el.getEnclosedElements().iterator();
		while (memberIter.hasNext()) {
			Element member = memberIter.next();

			if (AnnotationProcessorUtils.isMethod(member)) {
				ExecutableElement method = (ExecutableElement) member;
				if (AnnotationProcessorUtils.isJavaBeanGetter(method)
						|| AnnotationProcessorUtils.isJavaBeanSetter(method)) {
					// Property
					Iterator<? extends AnnotationMirror> annIter = member.getAnnotationMirrors().iterator();
					while (annIter.hasNext()) {
						AnnotationMirror ann = annIter.next();
						String annTypeName = ann.getAnnotationType().toString();
						if (annTypeName.startsWith("javax.persistence")) {
							return AnnotationProcessorUtils.getPropertyMembers(el);
						}
					}
				}
			}
		}
		return AnnotationProcessorUtils.getFieldMembers(el);
	}
	
	/**
	 * Method to find the next persistent supertype above this one.
	 * 
	 * @param processingEnv
	 *            ProcessingEnvironment instance
	 * @param element
	 *            The element
	 * @return Its next parent that is persistable (or null if no persistable
	 *         predecessors)
	 */
	public static TypeElement getPersistentSupertype(ProcessingEnvironment processingEnv, TypeElement element) {
		TypeMirror superType = element.getSuperclass();
		if (superType == null || "java.lang.Object".equals(element.toString())) {
			return null;
		}

		TypeElement superElement = (TypeElement) processingEnv.getTypeUtils().asElement(superType);
		if (AnnotationProcessorUtils.isJPAAnnotated(superElement)) {
			return superElement;
		} if (superElement == null) {
			return null;
		}
		return getPersistentSupertype(processingEnv, superElement);
	}
}