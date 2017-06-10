package com.bryndsey.songspark.dagger;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AndroidModule.class
})
public interface ApplicationComponent {
	
}
