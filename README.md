# pretty-university-schedule-awt

[![](https://jitpack.io/v/RebootSTR/pretty-university-schedule-awt.svg)](https://jitpack.io/#RebootSTR/pretty-university-schedule-awt)

This is a java-awt implementation [CORE](https://github.com/RebootSTR/pretty-university-schedule-core) for drawing university schedule.

## Instalation

+ Gradle
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
  
 dependencies {
	 implementation 'com.github.RebootSTR:pretty-university-schedule-awt:1.0'
 }
```
+ Maven
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.RebootSTR</groupId>
    <artifactId>pretty-university-schedule-awt</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage
2. Build `Schedule` with `SheduleBuilder`.
3. Pass `Schedule` in `AwtDrawer`.
4. Call `AwtDrawer::drawSchedule`.
5. Call `AwtDrawer::save` with `File` object, where you want save a shedule.

## Customize
Also you can customize some colors and sizes in generated schedule. To do this you should create new implementation from `AwtDrawer` and override some values. 
