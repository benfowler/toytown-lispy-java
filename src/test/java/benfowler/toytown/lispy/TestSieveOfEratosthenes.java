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

package benfowler.toytown.lispy;

import java.util.Arrays;
import java.util.List;

public class TestSieveOfEratosthenes extends AbstractExternalFileTest {

	@Override
	protected String getPathToScriptResource() {
		return "sieve.sls";
	}

	@Override
	protected Object getTestResult() {
	return list(2.0, 3.0, 5.0, 7.0, 11.0, 13.0, 17.0, 19.0, 23.0, 29.0,
		     31.0, 37.0, 41.0, 43.0, 47.0, 53.0, 59.0, 61.0, 67.0,
		     71.0, 73.0, 79.0, 83.0, 89.0, 97.0, 101.0, 103.0, 107.0,
		     109.0, 113.0, 127.0, 131.0, 137.0, 139.0, 149.0, 151.0,
		     157.0, 163.0, 167.0, 173.0, 179.0, 181.0, 191.0, 193.0,
		     197.0, 199.0);
	}

	private static List<Object> list(final Object... objects) {
		return Arrays.asList(objects);
	}

}
