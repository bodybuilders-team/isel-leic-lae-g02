package pt.isel.sample

import pt.isel.JsonProperty

data class Student(var nr: Int = 0, var name: String? = null)


data class Student2(val nr: Int = 0, val name: String? = null)

class Student3 {
	var name: String? = "John"
	var nr: Int = 3
	var t = 0.0;

	constructor(nr: Int) {
		this.nr = nr
	}

	constructor(t: Double, name: String) {
		this.t = t
		this.name = name
	}

	constructor(nr: Int, name: String) {
		this.nr = nr
		this.name = name
	}
}
