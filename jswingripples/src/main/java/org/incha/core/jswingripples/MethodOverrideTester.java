/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.incha.core.jswingripples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.incha.core.jswingripples.util.LRUMap;



public class MethodOverrideTester {
	private static class Substitutions {

		public static final Substitutions EMPTY_SUBST= new Substitutions();

		private HashMap<String, String[]> fMap;

		public Substitutions() {
			fMap= null;
		}

		public void addSubstitution(final String typeVariable, final String substitution, final String erasure) {
			if (fMap == null) {
				fMap= new HashMap<String, String[]>(3);
			}
			fMap.put(typeVariable, new String[] { substitution, erasure });
		}

		private String[] getSubstArray(final String typeVariable) {
			if (fMap != null) {
				return fMap.get(typeVariable);
			}
			return null;
		}

		public String getSubstitution(final String typeVariable) {
			final String[] subst= getSubstArray(typeVariable);
			if (subst != null) {
				return subst[0];
			}
			return null;
		}

		public String getErasure(final String typeVariable) {
			final String[] subst= getSubstArray(typeVariable);
			if (subst != null) {
				return subst[1];
			}
			return null;
		}
	}

	private IType fFocusType;
	private final TypeHierarchySupport hierarhySupport;

	private Map<IMethod, Substitutions> fMethodSubstitutions;
	private Map  <IType, Substitutions> fTypeVariableSubstitutions;

	public MethodOverrideTester(final IType focusType, final TypeHierarchySupport hierarhySupport) {
		fFocusType= focusType;
		this.hierarhySupport= hierarhySupport;
		fTypeVariableSubstitutions= null;
		fMethodSubstitutions= null;
	}

	public IType getFocusType() {
		return fFocusType;
	}


	public void setFocusType(final IType type) {
		if (fFocusType!=null && type==fFocusType) return;
		fFocusType=type;
		fTypeVariableSubstitutions= null;
		fMethodSubstitutions= null;
	}
	/**
	 * Finds the method that declares the given method. A declaring method is the 'original' method declaration that does
	 * not override nor implement a method. <code>null</code> is returned it the given method does not override
	 * a method. When searching, super class are examined before implemented interfaces.
	 * @param testVisibility If true the result is tested on visibility. Null is returned if the method is not visible.
	 * @throws JavaModelException
	 */
	public IMethod findDeclaringMethod(final IMethod overriding, final boolean testVisibility) throws JavaModelException {
		IMethod result= null;
		IMethod overridden= findOverriddenMethod(overriding, testVisibility);
		while (overridden != null) {
			result= overridden;
			overridden= findOverriddenMethod(result, testVisibility);
		}
		return result;
	}

	/**
	 * Finds all overrides
	 */
	public IMethod findAllDeclaringMethods(final IMethod overriding, final boolean testVisibility, final HashSet<IMethod> overrides) throws JavaModelException {

		IMethod result= null;
		IMethod overridden= findOverriddenMethod(overriding, testVisibility);


		while (overridden != null) {
			if (overridden.getResource()==null) return null;
			overrides.add(overridden);
			result= overridden;
			overridden= findOverriddenMethod(result, testVisibility);
		}
		return result;
	}

	/**
	 * Finds the method that is overridden by the given method.
	 * First the super class is examined and then the implemented interfaces.
	 * @param testVisibility If true the result is tested on visibility. Null is returned if the method is not visible.
	 * @throws JavaModelException
	 */
	public IMethod findOverriddenMethod(final IMethod overriding, final boolean testVisibility) throws JavaModelException {
		final int flags= overriding.getFlags();
		if (Flags.isPrivate(flags) || Flags.isStatic(flags) || overriding.isConstructor()) {
			return null;
		}

		final IType type= overriding.getDeclaringType();
		final IType superClass= hierarhySupport.getSuperclass(type);
		if (superClass != null) {
			//if (superClass.getResource()==null) return null;
			final IMethod res= findOverriddenMethodInHierarchy(superClass, overriding);
			if (res != null && !Flags.isPrivate(res.getFlags())) {
				if (!testVisibility || isVisibleInHierarchy(res, type.getPackageFragment())) {
					return res;
				}
			}
		}
		if (!overriding.isConstructor()) {
			final IType[] interfaces= hierarhySupport.getSuperInterfaces(type);
			for (int i= 0; i < interfaces.length; i++) {
				//if (interfaces[i].getResource()==null)  continue;
				final IMethod res= findOverriddenMethodInHierarchy(interfaces[i], overriding);
				if (res != null) {
					return res; // methods from interfaces are always public and therefore visible
				}
			}
		}
		return null;
	}

	/**
	 * Finds the directly overridden method in a type and its super types. First the super class is examined and then the implemented interfaces.
	 * With generics it is possible that 2 methods in the same type are overidden at the same time. In that case, the first overridden method found is returned.
	 * 	@param type The type to find methods in
	 * @param overriding The overriding method
	 * @return The first overridden method or <code>null</code> if no method is overridden
	 * @throws JavaModelException
	 */
	public IMethod findOverriddenMethodInHierarchy(final IType type, final IMethod overriding) throws JavaModelException {
		final IMethod method= findOverriddenMethodInType(type, overriding);
		if (method != null) {
			return method;
		}
		final IType superClass= hierarhySupport.getSuperclass(type);
		if (superClass != null) {
			//if (superClass.getResource()==null) return null;
			final IMethod res=  findOverriddenMethodInHierarchy(superClass, overriding);
			if (res != null) {
				return res;
			}
		}
		if (!overriding.isConstructor()) {
			final IType[] superInterfaces= hierarhySupport.getSuperInterfaces(type);
			for (int i= 0; i < superInterfaces.length; i++) {
				//if (superInterfaces[i].getResource()==null) continue;
				final IMethod res= findOverriddenMethodInHierarchy(superInterfaces[i], overriding);
				if (res != null) {
					return res;
				}
			}
		}
		return method;
	}

	/**
	 * Finds an overridden method in a type. WWith generics it is possible that 2 methods in the same type are overidden at the same time.
	 * In that case the first overridden method found is returned.
	 * @param overriddenType The type to find methods in
	 * @param overriding The overriding method
	 * @return The first overridden method or <code>null</code> if no method is overridden
	 * @throws JavaModelException
	 */
	public IMethod findOverriddenMethodInType(final IType overriddenType, final IMethod overriding) throws JavaModelException {
		final IMethod[] overriddenMethods= overriddenType.getMethods();
		for (int i= 0; i < overriddenMethods.length; i++) {
			if (isSubsignature(overriding, overriddenMethods[i])) {
				return overriddenMethods[i];
			}
		}
		return null;
	}

	/**
	 * Finds an overriding method in a type.
	 * @param overridingType The type to find methods in
	 * @param overridden The overridden method
	 * @return The overriding method or <code>null</code> if no method is overriding.
	 * @throws JavaModelException
	 */
	public IMethod findOverridingMethodInType(final IType overridingType, final IMethod overridden) throws JavaModelException {
		final IMethod[] overridingMethods= overridingType.getMethods();
		for (int i= 0; i < overridingMethods.length; i++) {
			if (isSubsignature(overridingMethods[i], overridden)) {
				return overridingMethods[i];
			}
		}
		return null;
	}

	/**
	 * Tests if a method is a subsignature of another method.
	 * @param overriding overriding method (m1)
	 * @param overridden overridden method (m2)
	 * @return <code>true</code> iff the method <code>m1</code> is a subsignature of the method <code>m2</code>.
	 * 		This is one of the requirements for m1 to override m2.
	 * 		Accessibility and return types are not taken into account.
	 * 		Note that subsignature is <em>not</em> symmetric!
	 * @throws JavaModelException
	 */
	public boolean isSubsignature(final IMethod overriding, final IMethod overridden) throws JavaModelException {
		if (!overridden.getElementName().equals(overriding.getElementName())) {
			return false;
		}
		final int nParameters= overridden.getNumberOfParameters();
		if (nParameters != overriding.getNumberOfParameters()) {
			return false;
		}

		if (!hasCompatibleTypeParameters(overriding, overridden)) {
			return false;
		}

		return nParameters == 0 || hasCompatibleParameterTypes(overriding, overridden);
	}

	private boolean hasCompatibleTypeParameters(final IMethod overriding, final IMethod overridden) throws JavaModelException {
		final ITypeParameter[] overriddenTypeParameters= overridden.getTypeParameters();
		final ITypeParameter[] overridingTypeParameters= overriding.getTypeParameters();
		final int nOverridingTypeParameters= overridingTypeParameters.length;
		if (overriddenTypeParameters.length != nOverridingTypeParameters) {
			return nOverridingTypeParameters == 0;
		}
		final Substitutions overriddenSubst= getMethodSubstitions(overridden);
		final Substitutions overridingSubst= getMethodSubstitions(overriding);
		for (int i= 0; i < nOverridingTypeParameters; i++) {
			final String erasure1= overriddenSubst.getErasure(overriddenTypeParameters[i].getElementName());
			final String erasure2= overridingSubst.getErasure(overridingTypeParameters[i].getElementName());
			if (erasure1 == null || !erasure1.equals(erasure2)) {
				return false;
			}
			// comparing only the erasure is not really correct: Need to compare all bounds, that can be in different order
			final int nBounds= overriddenTypeParameters[i].getBounds().length;
			if (nBounds > 1 && nBounds != overridingTypeParameters[i].getBounds().length) {
				return false;
			}
		}
		return true;
	}

	private boolean hasCompatibleParameterTypes(final IMethod overriding, final IMethod overridden) throws JavaModelException {
		final String[] overriddenParamTypes= overridden.getParameterTypes();
		final String[] overridingParamTypes= overriding.getParameterTypes();

		final String[] substitutedOverriding= new String[overridingParamTypes.length];
		boolean testErasure= false;

		for (int i= 0; i < overridingParamTypes.length; i++) {
			final String overriddenParamSig= overriddenParamTypes[i];
			final String overriddenParamName= getSubstitutedTypeName(overriddenParamSig, overridden);
			final String overridingParamName= getSubstitutedTypeName(overridingParamTypes[i], overriding);
			substitutedOverriding[i]= overridingParamName;
			if (!overriddenParamName.equals(overridingParamName)) {
				testErasure= true;
				break;
			}
		}
		if (testErasure) {
			for (int i= 0; i < overridingParamTypes.length; i++) {
				final String overriddenParamSig= overriddenParamTypes[i];
				final String overriddenParamName= getErasedTypeName(overriddenParamSig, overridden);
				String overridingParamName= substitutedOverriding[i];
				if (overridingParamName == null)
					overridingParamName= getSubstitutedTypeName(overridingParamTypes[i], overriding);
				if (!overriddenParamName.equals(overridingParamName)) {
					return false;
				}
			}
		}
		return true;
	}

	private String getVariableSubstitution(final IMember context, final String variableName) throws JavaModelException {
		IType type;
		if (context instanceof IMethod) {
			final String subst= getMethodSubstitions((IMethod) context).getSubstitution(variableName);
			if (subst != null) {
				return subst;
			}
			type= context.getDeclaringType();
		} else {
			type= (IType) context;
		}
		final String subst= getTypeSubstitions(type).getSubstitution(variableName);
		if (subst != null) {
			return subst;
		}
		return variableName; // not a type variable
	}

	private String getVariableErasure(final IMember context, final String variableName) throws JavaModelException {
		IType type;
		if (context instanceof IMethod) {
			final String subst= getMethodSubstitions((IMethod) context).getErasure(variableName);
			if (subst != null) {
				return subst;
			}
			type= context.getDeclaringType();
		} else {
			type= (IType) context;
		}
		final String subst= getTypeSubstitions(type).getErasure(variableName);
		if (subst != null) {
			return subst;
		}
		return variableName; // not a type variable
	}

	/*
	 * Returns the substitutions for a method's type parameters
	 */
	private Substitutions getMethodSubstitions(final IMethod method) throws JavaModelException {
		if (fMethodSubstitutions == null) {
			fMethodSubstitutions= new LRUMap<IMethod, Substitutions>(3);
		}

		Substitutions s= fMethodSubstitutions.get(method);
		if (s == null) {
			final ITypeParameter[] typeParameters= method.getTypeParameters();
			if (typeParameters.length == 0) {
				s= Substitutions.EMPTY_SUBST;
			} else {
				final IType instantiatedType= method.getDeclaringType();
				s= new Substitutions();
				for (int i= 0; i < typeParameters.length; i++) {
					final ITypeParameter curr= typeParameters[i];
					s.addSubstitution(curr.getElementName(), '+' + String.valueOf(i), getTypeParameterErasure(curr, instantiatedType));
				}
			}
			fMethodSubstitutions.put(method, s);
		}
		return s;
	}

	/*
	 * Returns the substitutions for a type's type parameters
	 */
	private Substitutions getTypeSubstitions(final IType type) throws JavaModelException {
		if (fTypeVariableSubstitutions == null) {
			fTypeVariableSubstitutions= new HashMap<IType, Substitutions>();
			computeSubstitutions(fFocusType, null, null);
		}
		final Substitutions subst= fTypeVariableSubstitutions.get(type);
		if (subst == null) {
			return Substitutions.EMPTY_SUBST;
		}
		return subst;
	}

	private void computeSubstitutions(final IType instantiatedType, final IType instantiatingType, final String[] typeArguments) throws JavaModelException {
		final Substitutions s= new Substitutions();
		fTypeVariableSubstitutions.put(instantiatedType, s);

		final ITypeParameter[] typeParameters= instantiatedType.getTypeParameters();

		if (instantiatingType == null) { // the focus type
			for (int i= 0; i < typeParameters.length; i++) {
				final ITypeParameter curr= typeParameters[i];
				// use star to make type variables different from type refs
				s.addSubstitution(curr.getElementName(), '*' + curr.getElementName(), getTypeParameterErasure(curr, instantiatedType));
			}
		} else {
			if (typeParameters.length == typeArguments.length) {
				for (int i= 0; i < typeParameters.length; i++) {
					final ITypeParameter curr= typeParameters[i];
					final String substString= getSubstitutedTypeName(typeArguments[i], instantiatingType); // substitute in the context of the instantiatingType
					final String erasure= getErasedTypeName(typeArguments[i], instantiatingType); // get the erasure from the type argument
					s.addSubstitution(curr.getElementName(), substString, erasure);
				}
			} else if (typeArguments.length == 0) { // raw type reference
				for (int i= 0; i < typeParameters.length; i++) {
					final ITypeParameter curr= typeParameters[i];
					final String erasure= getTypeParameterErasure(curr, instantiatedType);
					s.addSubstitution(curr.getElementName(), erasure, erasure);
				}
			} else {
				// code with errors
			}
		}
		final String superclassTypeSignature= instantiatedType.getSuperclassTypeSignature();
		if (superclassTypeSignature != null) {
			final String[] superTypeArguments= Signature.getTypeArguments(superclassTypeSignature);
			final IType superclass= hierarhySupport.getSuperclass(instantiatedType);
			if (superclass != null && !fTypeVariableSubstitutions.containsKey(superclass)) {
				computeSubstitutions(superclass, instantiatedType, superTypeArguments);
			}
		}
		final String[] superInterfacesTypeSignature= instantiatedType.getSuperInterfaceTypeSignatures();
		final int nInterfaces= superInterfacesTypeSignature.length;
		if (nInterfaces > 0) {
			final IType[] superInterfaces= hierarhySupport.getSuperInterfaces(instantiatedType);
			if (superInterfaces.length == nInterfaces) {
				for (int i= 0; i < nInterfaces; i++) {
					final String[] superTypeArguments= Signature.getTypeArguments(superInterfacesTypeSignature[i]);
					final IType superInterface= superInterfaces[i];
					if (!fTypeVariableSubstitutions.containsKey(superInterface)) {
						computeSubstitutions(superInterface, instantiatedType, superTypeArguments);
					}
				}
			}
		}
	}

	private String getTypeParameterErasure(final ITypeParameter typeParameter, final IType context) throws JavaModelException {
		final String[] bounds= typeParameter.getBounds();
		if (bounds.length > 0) {
			return getSubstitutedTypeName(Signature.createTypeSignature(bounds[0], false), context);
		}
		return "Object"; //$NON-NLS-1$
	}


	/**
	 * Translates the type signature to a 'normalized' type name where all variables are substituted for the given type or method context.
	 * The returned name contains only simple names and can be used to compare against other substituted type names
	 * @param typeSig The type signature to translate
	 * @param context The context for the substitution
	 * @return a type name
	 * @throws JavaModelException
	 */
	private String getSubstitutedTypeName(final String typeSig, final IMember context) throws JavaModelException {
		return internalGetSubstitutedTypeName(typeSig, context, false, new StringBuffer()).toString();
	}

	private String getErasedTypeName(final String typeSig, final IMember context) throws JavaModelException {
		return internalGetSubstitutedTypeName(typeSig, context, true, new StringBuffer()).toString();
	}

	private StringBuffer internalGetSubstitutedTypeName(final String typeSig, final IMember context, final boolean erasure, final StringBuffer buf) throws JavaModelException {
		final int sigKind= Signature.getTypeSignatureKind(typeSig);
		switch (sigKind) {
			case Signature.BASE_TYPE_SIGNATURE:
				return buf.append(Signature.toString(typeSig));
			case Signature.ARRAY_TYPE_SIGNATURE:
				internalGetSubstitutedTypeName(Signature.getElementType(typeSig), context, erasure, buf);
				for (int i= Signature.getArrayCount(typeSig); i > 0; i--) {
					buf.append('[').append(']');
				}
				return buf;
			case Signature.CLASS_TYPE_SIGNATURE: {
				final String erasureSig= Signature.getTypeErasure(typeSig);
				final String erasureName= Signature.getSimpleName(Signature.toString(erasureSig));

				final char ch= erasureSig.charAt(0);
				if (ch == Signature.C_RESOLVED) {
					buf.append(erasureName);
				} else if (ch == Signature.C_UNRESOLVED) { // could be a type variable
					if (erasure) {
						buf.append(getVariableErasure(context, erasureName));
					} else {
						buf.append(getVariableSubstitution(context, erasureName));
					}
				} else {
					Assert.isTrue(false, "Unknown class type signature"); //$NON-NLS-1$
				}
				if (!erasure) {
					final String[] typeArguments= Signature.getTypeArguments(typeSig);
					if (typeArguments.length > 0) {
						buf.append('<');
						for (int i= 0; i < typeArguments.length; i++) {
							if (i > 0) {
								buf.append(',');
							}
							internalGetSubstitutedTypeName(typeArguments[i], context, erasure, buf);
						}
						buf.append('>');
					}
				}
				return buf;
			}
			case Signature.TYPE_VARIABLE_SIGNATURE:
				final String varName= Signature.toString(typeSig);
				if (erasure) {
					return buf.append(getVariableErasure(context, varName));
				} else {
					return buf.append(getVariableSubstitution(context, varName));
				}
			case Signature.WILDCARD_TYPE_SIGNATURE: {
				buf.append('?');
				final char ch= typeSig.charAt(0);
				if (ch == Signature.C_STAR) {
					return buf;
				} else if (ch == Signature.C_EXTENDS) {
					buf.append(" extends "); //$NON-NLS-1$
				} else {
					buf.append(" super "); //$NON-NLS-1$
				}
				return internalGetSubstitutedTypeName(typeSig.substring(1), context, erasure, buf);
			}
			case Signature.CAPTURE_TYPE_SIGNATURE:
				return internalGetSubstitutedTypeName(typeSig.substring(1), context, erasure, buf);
			default:
				Assert.isTrue(false, "Unhandled type signature kind"); //$NON-NLS-1$
				return buf;
		}
	}

	/**
	 * Evaluates if a member in the focus' element hierarchy is visible from
	 * elements in a package.
	 * @param member The member to test the visibility for
	 * @param pack The package of the focus element focus
	 * @return returns <code>true</code> if the member is visible from the package
	 * @throws JavaModelException thrown when the member can not be accessed
	 */
	public static boolean isVisibleInHierarchy(final IMember member, final IPackageFragment pack) throws JavaModelException {
		final int type= member.getElementType();
		if  (type == IJavaElement.INITIALIZER ||  (type == IJavaElement.METHOD && member.getElementName().startsWith("<"))) { //$NON-NLS-1$
			return false;
		}

		final int otherflags= member.getFlags();

		final IType declaringType= member.getDeclaringType();
		if (Flags.isPublic(otherflags) || Flags.isProtected(otherflags) || (declaringType != null && declaringType.isInterface())) {
			return true;
		} else if (Flags.isPrivate(otherflags)) {
			return false;
		}

		final IPackageFragment otherpack= (IPackageFragment) member.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
		return (pack != null && pack.equals(otherpack));
	}


}
