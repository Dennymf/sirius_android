// ITestService.aidl
package ru.mail.park.advandroid.helloword.lection2;


interface ITestService {
    String getString();
    oneway void getStringAsync();
    void bindActivity(IBinder callback);
}
