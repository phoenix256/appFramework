# appframework
Framework for easy and fast Android Development

You can quickly add this framework into your application with this three easy step:

1) Open Android Studio Terminal tab and type:  
  
  git submodule init  
  git submodule update  
  git submodule add https://github.com/phoenix256/appFramework.git  

![alt tag](https://raw.githubusercontent.com/phoenix256/appFramework/master/setup_help/git%20submodule%20init%20image.png)

![alt tag](https://raw.githubusercontent.com/phoenix256/appFramework/master/setup_help/git%20submodule%20update%20image.png)

![alt tag](https://raw.githubusercontent.com/phoenix256/appFramework/dev/setup_help/git%20submodule%20add%20image.png)

2) You will need another repository to be able to use all of the features and we will also need to add that with:  

  git submodule add https://github.com/phoenix256/collectionPicker.git

![alt tag](https://raw.githubusercontent.com/phoenix256/appFramework/dev/setup_help/git%20submodule%20add%20image%202.png)

3) After those installed successfully, you might need to refresh dependencies with:   

  ./gradlew build --refresh-dependencies

![alt tag](https://raw.githubusercontent.com/phoenix256/appFramework/dev/setup_help/gradlew%20build%20refresh%20dependencies.png)

4) After syncing gradle and rebuilding project, you are good to go!

Thanks
