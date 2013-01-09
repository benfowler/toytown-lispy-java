/*
 *  Toytown -- a reimplementation of Peter Norvig's lis.py interpreter for
 *  Python.
 *
 *  Copyright (C) 2012-2013  Benjamin J. Fowler.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package au.id.bjf.toylisp;

import java.util.List;

public class Proc {

	private final List<String> formalArguments;

	private final Environment environment;

	private final Object body;

	private boolean boundAllArgsAsList = false;

	public Proc(final List<String> formalArguments,
			final Environment environment, final Object body,
			final boolean boundAllArgsAsList) {
		this(formalArguments, environment, body);
		this.boundAllArgsAsList = boundAllArgsAsList;
	}

	public Proc(final List<String> formalArguments,
			final Environment environment, final Object body) {
		this.formalArguments = formalArguments;
		this.environment = environment;
		this.body = body;
	}

	public List<String> getFormalArguments() {
		return formalArguments;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Object getBody() {
		return body;
	}

	public boolean isBoundAllArgsAsList() {
		return boundAllArgsAsList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(lambda ");

		if (formalArguments != null) {
			builder.append(formalArguments.toString());
			if (boundAllArgsAsList) {
				builder.append("*");
			}
		} else {
			builder.append("<<null formalArgs>>");
		}

		builder.append(" ")
			.append(body != null ? body.toString() : "<<null body>>")
			.append(")");
		return builder.toString();
	}

}
