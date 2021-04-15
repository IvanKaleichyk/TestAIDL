//package com.koleychik.aidl;

//import com.koleychik.aidl.Sum;

interface Calculator{
    oneway void sum(int first, int second, AsyncCallback callback);
}