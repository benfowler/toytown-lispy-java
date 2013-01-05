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

public enum Errors {

	UNEXPECTED_TOKEN("Unexpected token. Expected %s, but got %s"),
	MISSING_PARENTHESIS("Missing parenthesis"),
	PREMATURE_END_OF_PROGRAM("Premature end of program"),
	TOKEN_AFTER_END_OF_PROGRAM("Token after end of program"),
	INVALID_NUMBER_OF_ARGUMENTS("Illegal number of arguments.  Expected at " +
			"least %d, at most %d arguments; got %d;"),
	NOT_YET_IMPLEMENTED("Not yet implemented"),
	CANNOT_EVAL("Cannot eval object of type %s: got '%s'"),
	EXPECTED_SYMBOL_OR_SPECIAL_FORM("Expected a symbol or special form, " +
			"but got '%s'"),
	BAD_IF_CONDITION("IF condition should evaluate to numeric value; " +
			"but got '%s'"),
	UNKNOWN_SYMBOL("Unknown symbol '%s'"),
	SYMBOL_MUST_BE_DEFINED("Symbol '%s' must have already been defined, " +
			"either with a 'define' or as a proc argument"),
	EXPECTED_LIST_OF_ARGUMENTS("Expected either a symbol or a list of symbols " +
			"for argument list in lambda expression, got '%s'"),
	INTERNAL_ERROR("Internal error: %s"),
	INSTANCE_OR_CLASSNAME_EXPECTED("Instance or classname expected for " +
			"dot-special form; instead got '%s'"),
	INSTANCE_OR_CLASSNAME_NOT_FOUND("Class or instance name '%s' not found"),
	BAD_METHOD_INVOCATION("Bad method invocation: got a %s while invoking " +
			"%s.%s(): %s"),
	NONSTATIC_CALL_IN_STATIC_CTX("Non-static method called in " +
			"static context: %s"),
	SYMBOL_EXPECTED("Symbol expected"),
	PROC_EXPECTED("Proc expected"),
	SYMBOL_NOT_FOUND("Symbol '%s' not found");

	private String parameterizedMessage;

	Errors(final String parameterizedMessage) {
		this.parameterizedMessage = parameterizedMessage;
	}

	public String getMessage(final Object ... args) {
		return String.format(parameterizedMessage, args);
	}

}
