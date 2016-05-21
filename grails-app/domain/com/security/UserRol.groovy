package com.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class UserRol implements Serializable {

	private static final long serialVersionUID = 1

	User user
	Rol rol

	@Override
	boolean equals(other) {
		if (other instanceof UserRol) {
			other.userId == user?.id && other.rolId == rol?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (rol) builder.append(rol.id)
		builder.toHashCode()
	}

	static UserRol get(long userId, long rolId) {
		criteriaFor(userId, rolId).get()
	}

	static boolean exists(long userId, long rolId) {
		criteriaFor(userId, rolId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long rolId) {
		UserRol.where {
			user == User.load(userId) &&
			rol == Rol.load(rolId)
		}
	}

	static UserRol create(User user, Rol rol) {
		def instance = new UserRol(user: user, rol: rol)
		instance.save()
		instance
	}

	static boolean remove(User u, Rol r) {
		if (u != null && r != null) {
			UserRol.where { user == u && rol == r }.deleteAll()
		}
	}

	static int removeAll(User u) {
		u == null ? 0 : UserRol.where { user == u }.deleteAll()
	}

	static int removeAll(Rol r) {
		r == null ? 0 : UserRol.where { rol == r }.deleteAll()
	}

	static constraints = {
		rol validator: { Rol r, UserRol ur ->
			if (ur.user?.id) {
				UserRol.withNewSession {
					if (UserRol.exists(ur.user.id, r.id)) {
						return ['userRole.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'rol']
		version false
	}
}
