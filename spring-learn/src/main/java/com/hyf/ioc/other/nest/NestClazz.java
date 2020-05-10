package com.hyf.ioc.other.nest;

import java.lang.reflect.Constructor;

/**
 * @author baB_hyf
 * @date 2020/03/27
 */
public class NestClazz {

	class Nest{

	}

}

class CreateNest {
	public static void main(String[] args) throws Exception {
		Constructor<NestClazz.Nest> constructor = NestClazz.Nest.class.getConstructor(NestClazz.class);
		System.out.println(constructor);
	}
}
