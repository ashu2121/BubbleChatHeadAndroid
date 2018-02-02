# BubbleChatHeadAndroid
#Usage of library :

This libray will use to display buble of profile photo at above of app. It is uses Dialog to open bubble.  

    Download this library 
    Add this library in libs folder of your project
    Add dependency to your project
    In app level build.gradle file :
    compile project(':bubblechathead')       

# Permission needed.
> Dialog Permsision needed to showo Bubble. 

# API levele
> Lowest  API level for this library is API 8

# How to use 
After adding dependency you need to create new refrence of chat head service :
ChatHeadService chatHeadService=new ChatHeadService();

For creating bubble chat head use given method with passing activity context :
>chatHeadService.initializeBubbleView(context);

For customize the shadow color of bubble chat head use this method with passing activity context :
>chatHeadService.setChatHeadShadowColor(context,colorCode);

For customize the depth or transparency of the shadow of bubble chat head, There are seven layers. You must need to define percentage of transparency for each layer.  You can use this method with passing activity context like:

>chatHeadService.setShadowDepthLayers(context,layer1,layer2,layer3,layer4,layer5,layer6,layer7);

Layer value must be the Integer(varies from 0 to 100).

For example :
>chatHeadService.setShadowDepthLayers(context,0,5,5,15,30,45,60);
First layer will be the outer layer of the bubble chat head.

`**For avoid Memory Leak, In onDestroy() method of your activity, you have to call this method :**`
>chatHeadService.stopService(context);
