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

/**
 * Special forms supported by the interpreter
 */
public enum SpecialForm {

	QUOTE("quote"),
	IF("if"),
	SET_("set!"),
	DEFINE("define"),
	LAMBDA("lambda"),
	BEGIN("begin"),
	PROC("proc"),
	DOT(".");

	private final String literalVal;

	SpecialForm(final String literalVal) {
		this.literalVal = literalVal;
	}

	public String getLiteralVal() {
		return literalVal;
	}

	public static SpecialForm getByLiteralVal(final String literalVal) {
		for (SpecialForm sf : values()) {
			if (sf.getLiteralVal().equals(literalVal)) {
				return sf;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return literalVal;
	}

}
