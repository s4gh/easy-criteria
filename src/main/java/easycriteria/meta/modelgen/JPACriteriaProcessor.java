package easycriteria.meta.modelgen;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.AccessType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.tools.JavaFileObject;

import easycriteria.meta.modelgen.AnnotationProcessorUtils.TypeCategory;

@SupportedAnnotationTypes({ "javax.persistence.Entity", "javax.persistence.Embeddable", "javax.persistence.MappedSuperclass" })
public class JPACriteriaProcessor extends AbstractProcessor {
	private static final String CLASS_NAME_SUFFIX = "_";
	private static final String CLASS_NAME_PREFIX = "Q";

	private static final String CODE_INDENT = "    ";

	Types typesHandler;

	@SuppressWarnings("rawtypes")
	protected static Class[] annotationsWithTargetEntity = new Class[] { OneToOne.class, OneToMany.class,
			ManyToOne.class, ManyToMany.class };

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
	 * javax.annotation.processing.RoundEnvironment)
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			return false;
		}

		typesHandler = processingEnv.getTypeUtils();
		Set<? extends Element> elements = roundEnv.getRootElements();
		for (Element e : elements) {
			if (e instanceof TypeElement) {
				processClass((TypeElement) e);
			}
		}
		return false;
	}

	private String generateMetaClassName(String className) {
		String pkgName = className.substring(0, className.lastIndexOf('.'));
		String classSimpleName = className.substring(className.lastIndexOf('.') + 1);
		String classNameNew = pkgName + "." + CLASS_NAME_PREFIX + classSimpleName + CLASS_NAME_SUFFIX;
		return classNameNew;
	}

	/**
	 * Handler for processing a JPA annotated class to create the criteria class
	 * stub.
	 *
	 * @param el
	 *            The class element
	 */
	protected void processClass(TypeElement el) {
		if (el == null || !AnnotationProcessorUtils.isJPAAnnotated(el)) {
			return;
		}

		// TODO Set imports to only include the classes we require
		// TODO Set references to other classes to be the class name and put the
		// package in the imports
		// TODO Support specification of the location for writing the class
		// source files
		Elements elementUtils = processingEnv.getElementUtils();
		String className = elementUtils.getBinaryName(el).toString();
		String pkgName = className.substring(0, className.lastIndexOf('.'));
		String classSimpleName = className.substring(className.lastIndexOf('.') + 1);
		String classNameNew = generateMetaClassName(className);

		System.out.println("EasyCriteria : JPA Criteria - " + className + " -> " + classNameNew);

		try {
			JavaFileObject javaFile = processingEnv.getFiler().createSourceFile(classNameNew);
			Writer w = javaFile.openWriter();
			try {
				w.append("package " + pkgName + ";\n");
				w.append("\n");
				w.append("import easycriteria.meta.*;\n");
				w.append("\n");
				w.append("public class " + CLASS_NAME_PREFIX + classSimpleName + CLASS_NAME_SUFFIX);

				w.append(" extends ObjectAttribute<").append(classSimpleName).append(">");

				w.append("");
				w.append("{\n");

				// Add constructors
				w.append(CODE_INDENT)
						.append("public " + CLASS_NAME_PREFIX + classSimpleName + CLASS_NAME_SUFFIX + "() {");
				w.append("\n");
				w.append(CODE_INDENT).append(CODE_INDENT).append("super(null, null, ").append(classSimpleName).append(".class);");
				w.append("\n");
				w.append(CODE_INDENT).append("}");
				w.append("\n");

				w.append(CODE_INDENT).append("public " + CLASS_NAME_PREFIX + classSimpleName + CLASS_NAME_SUFFIX
						+ "(String attribute, EntityPathNode parent) {");
				w.append("\n");
				w.append(CODE_INDENT).append(CODE_INDENT).append("super(attribute, parent, ").append(classSimpleName).append(".class);");;
				w.append("\n");
				w.append(CODE_INDENT).append("}");
				w.append("\n");

				generateMetaModelDataForClass(el, w, classSimpleName);

				w.append("}\n");
				w.flush();
			} finally {
				w.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, TypeMirror> getGenericLookups(TypeElement el) {
		Map<String, TypeMirror> genericLookups = null;
		List<? extends TypeParameterElement> elTypeParams = el.getTypeParameters();
		for (TypeParameterElement elTypeParam : elTypeParams) {
			List<? extends TypeMirror> elTypeBounds = elTypeParam.getBounds();
			if (elTypeBounds != null && !elTypeBounds.isEmpty()) {
				genericLookups = new HashMap<String, TypeMirror>();
				genericLookups.put(elTypeParam.toString(), elTypeBounds.get(0));
			}
		}
		return genericLookups;
	}

	private void generateMetaModelDataForClass(TypeElement el, Writer w, String classSimpleName) throws IOException {

		TypeElement superEl = AnnotationProcessorUtils.getPersistentSupertype(processingEnv, el);
		Map<String, TypeMirror> genericLookups = getGenericLookups(el);
		if (superEl != null) {
			generateMetaModelDataForClass(superEl, w, classSimpleName);
		}

		List<? extends Element> members = getClassMembers(el);

		if (members != null) {
			processClassMembers(classSimpleName, genericLookups, w, members);
		}
	}

	private List<? extends Element> getClassMembers(TypeElement el) {
		List<? extends Element> members;
		// Find the members to use for persistence processing
		AccessType clsAccessType = (AccessType) AnnotationProcessorUtils.getValueForAnnotationAttribute(el,
				AccessType.class, "value");
		if (clsAccessType == AccessType.FIELD) {
			// Only use fields
			members = AnnotationProcessorUtils.getFieldMembers(el);
		} else if (clsAccessType == AccessType.PROPERTY) {
			// Only use properties
			members = AnnotationProcessorUtils.getPropertyMembers(el);
		} else {
			// Default access type - whichever type (field or method) is
			// annotated first
			members = AnnotationProcessorUtils.getDefaultAccessMembers(el);
		}
		return members;
	}

	private void processClassMembers(String classSimpleName, Map<String, TypeMirror> genericLookups, Writer w,
			List<? extends Element> members) throws IOException {
		Iterator<? extends Element> iter = members.iterator();
		while (iter.hasNext()) {
			Element member = iter.next();
			boolean isTransient = false;
			List<? extends AnnotationMirror> annots = member.getAnnotationMirrors();
			if (annots != null) {
				Iterator<? extends AnnotationMirror> annotIter = annots.iterator();
				while (annotIter.hasNext()) {
					AnnotationMirror annot = annotIter.next();
					if (annot.getAnnotationType().toString().equals(Transient.class.getName())) {
						// Ignore this
						isTransient = true;
						break;
					}
				}
			}

			if (!isTransient) {
				if (member.getKind() == ElementKind.FIELD || (member.getKind() == ElementKind.METHOD
						&& AnnotationProcessorUtils.isJavaBeanGetter((ExecutableElement) member))) {
					TypeMirror type = AnnotationProcessorUtils.getDeclaredType(member);
					TypeCategory cat = null;
					String memberName = AnnotationProcessorUtils.getMemberName(member);

					if (AnnotationProcessorUtils.isFieldJPAAEntity(processingEnv, member)) {
						cat = TypeCategory.JPA_ENTITY_ATTRIBUTE;
						writePropertyAsEntity(w, type, memberName);
					} else {
						String typeName = AnnotationProcessorUtils.getDeclaredTypeName(processingEnv, type, true);
						cat = AnnotationProcessorUtils.getTypeCategoryForTypeMirror(typeName);
						writePropertyAttribute(classSimpleName, genericLookups, w, member, type, cat, memberName);
					}
				}
			}
		}
	}

	private void writePropertyAsEntity(Writer w, TypeMirror type, String memberName) throws IOException {

		String pkgName = type.toString().substring(0, type.toString().lastIndexOf('.'));
		String entitySimpleName = type.toString().substring(type.toString().lastIndexOf('.') + 1);
		String classNameNew = pkgName + "." + CLASS_NAME_PREFIX + entitySimpleName + CLASS_NAME_SUFFIX;

		w.append(CODE_INDENT + "public " + classNameNew);
		w.append(" " + memberName + " = new " + classNameNew + "(\"" + memberName + "\", this);");
		w.append("\n");
	}

	private void writePropertyAttribute(String classSimpleName, Map<String, TypeMirror> genericLookups, Writer w,
			Element member, TypeMirror type, TypeCategory cat, String memberName) throws IOException {

		String typeName = type.toString();

		w.append(CODE_INDENT).append("public " + cat.getTypeName()).append("<" + classSimpleName + ", ");
		if (cat == TypeCategory.ATTRIBUTE || cat == TypeCategory.DATE_ATTRIBUTE || cat == TypeCategory.NUMBER_ATTRIBUTE
				|| cat == TypeCategory.STRING_ATTRIBUTE) {
			if (type.getKind() == TypeKind.DECLARED && type instanceof DeclaredType && type instanceof TypeVariable)
			{
				// This was needed to detect such as a field with a Bean Validation 2.0 @NotNull, which comes through as
				// "(@javax.validation.constraints.NotNull :: theUserType)", so this converts that to "theUserType".
				// TODO Is this the best way to trap that case ? (i.e "TypeVariable")?! probably not, so find a better way
				// Note that this is also a WildcardType, ReferenceType, ArrayType
				type = ((DeclaredType)type).asElement().asType();
			}
			if (type instanceof PrimitiveType) {
				if (type.toString().equals("long")) {
					typeName = "Long";
				} else if (type.toString().equals("int")) {
					typeName = "Integer";
				} else if (type.toString().equals("short")) {
					typeName = "Short";
				} else if (type.toString().equals("float")) {
					typeName = "Float";
				} else if (type.toString().equals("double")) {
					typeName = "Double";
				} else if (type.toString().equals("char")) {
					typeName = "Character";
				} else if (type.toString().equals("byte")) {
					typeName = "Byte";
				} else if (type.toString().equals("boolean")) {
					typeName = "Boolean";
				}
				w.append(typeName);
			} else {
				typeName = type.toString();
				TypeMirror target = null;
				for (int i = 0; i < annotationsWithTargetEntity.length; i++) {
					Object targetValue = AnnotationProcessorUtils.getValueForAnnotationAttribute(member,
							annotationsWithTargetEntity[i], "targetEntity");
					if (targetValue != null) {
						target = (TypeMirror) targetValue;
						break;
					}
				}
				if (target != null) {
					typeName = target.toString();
				} else if (genericLookups != null && genericLookups.containsKey(typeName)) {
					// This is a generic type, so replace with the bound type
					// equates to "T extends MyOtherType" and putting
					// "MyOtherType" in
					typeName = genericLookups.get(typeName).toString();
				}
				w.append(typeName);
			}
		} else if (cat == TypeCategory.MAP) {
			TypeMirror keyType = getTypeParameter(member, type, 0, false);
			String keyTypeName = AnnotationProcessorUtils.getDeclaredTypeName(processingEnv, keyType, true);
			TypeMirror valueType = getTypeParameter(member, type, 1, true);
			String valueTypeName = AnnotationProcessorUtils.getDeclaredTypeName(processingEnv, valueType, true);
			w.append(keyTypeName + ", " + valueTypeName);
			w.append("> " + memberName + " = new " + cat.getTypeName() + "<>(\"" + memberName + "\", this, " + classSimpleName + ".class, " + keyTypeName  + ".class, " + valueTypeName + ".class);\n");
		} else {
			TypeMirror elementType = getTypeParameter(member, type, 0, true);
			String elementTypeName = AnnotationProcessorUtils.getDeclaredTypeName(processingEnv, elementType, true);
			w.append(elementTypeName);
			if (cat == TypeCategory.LIST || cat == TypeCategory.SET || cat == TypeCategory.COLLECTION) {
				typeName = elementTypeName;
			}
		}

		if (cat != TypeCategory.MAP) {
			w.append("> " + memberName + " = new " + cat.getTypeName() + "<>(\"" + memberName + "\", this, " + typeName + ".class);\n");
		}
	}

	TypeMirror getTypeParameter(Element element, TypeMirror type, int position, boolean checkTarget) {
		if (type.getKind() == TypeKind.ARRAY) {
			return ((ArrayType) type).getComponentType();
		}
		if (type.getKind() != TypeKind.DECLARED) {
			return null;
		}

		if (checkTarget) {
			// Check annotations for a value
			TypeMirror target = null;
			for (int i = 0; i < annotationsWithTargetEntity.length; i++) {
				Object targetValue = AnnotationProcessorUtils.getValueForAnnotationAttribute(element,
						annotationsWithTargetEntity[i], "targetEntity");
				if (targetValue != null) {
					target = (TypeMirror) targetValue;
					break;
				}
			}
			if (target != null) {
				return target;
			}
		}

		// Use generics
		List<? extends TypeMirror> params = ((DeclaredType) type).getTypeArguments();
		TypeMirror param = (params == null || params.size() < position + 1) ? typesHandler.getNullType()
				: params.get(position);
		return param;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}
}