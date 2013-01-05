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

import java.util.HashMap;

public class Environment extends HashMap<String, Object> {

	/** Default serial version ID */
	private static final long serialVersionUID = 1L;

	private Environment outer = null;

	/**
	 * Build an environment, which delegates to the given outer environment
	 * @param outer
	 */
	public Environment(final Environment outer) {
		this.outer = outer;
	}

	@Override
	public Object get(final Object key) {
		Object result = super.get(key);
		if (result == null && outer != null) {
			result = outer.get(key);
		}
		return result;
	}

}
