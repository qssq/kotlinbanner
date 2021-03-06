package qssq666.cn.kotlin.bannerdemo

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.qssq666.banner.Banner
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import qssq666.cn.kotlin.bannerdemo.transformer.*
import java.util.*

class MainActivity : Activity() {

    private var imageLoader: ImageLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageLoader = ImageLoader.getInstance()
        imageLoader!!.init(ImageLoaderConfiguration.createDefault(this))
        initBanner(banner, 0);
        initBanner(banner2, 1);


    }

    private fun initBanner(bannerCurrent: Banner, type: Int) {
        val listImgInfo = ArrayList<MyImageInfo>()
        listImgInfo.add(MyImageInfo("图片1", "http://pic.ffpic.com/files/2012/1109/1106fengyeer07.jpg"))
        listImgInfo.add(MyImageInfo("美女2", "http://f.hiphotos.baidu.com/image/pic/item/a044ad345982b2b725c274ca33adcbef76099b5b.jpg"))
        listImgInfo.add(MyImageInfo("美女3", "http://a.hiphotos.baidu.com/image/pic/item/7a899e510fb30f24a23edc1cca95d143ad4b030c.jpg"))
        listImgInfo.add(MyImageInfo("美女4", "http://g.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828b38f1a605f91f4134960a5a01.jpg"))

        bannerCurrent.setPointSize(8)//dp
        bannerCurrent.setPointMargin(20)//dp
        //你能拿到这个对象 你还不知道怎么调间距吗？xml 或者 代码编写 都可以.你喜欢哪个姿势呢?
        /*     TextView titleView = banner.getTitleView();
        ViewGroup titleGroup = banner.getTitleGroup();
        LinearLayout pointViewGroup = banner.getPointViewGroup();*/

        bannerCurrent.setItem(listImgInfo)
        bannerCurrent.autoScroll = true;
        bannerCurrent.scrollTime = 1000;
        bannerCurrent.setNeedPoint(true)
        bannerCurrent.setNeedTitle(true)
        bannerCurrent.setBindHolderProvider(object : Banner.OnViewBindHolderProvider<Banner.IImgInfo> {
            override fun onCreateView(group: ViewGroup, model: Banner.IImgInfo, position: Int): View {
                if (type == 0) {
                    var view = TextView(group.context);
                    val params = ViewPager.LayoutParams()
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    view.setTextSize(30f);
                    view.setBackgroundColor(Color.CYAN);
                    view.setTextColor(Color.BLACK);
                    view.gravity = Gravity.CENTER;
                    view.layoutParams = params;
                    view.setText(model.bannerTitle + "我是 文本 ")
                    return view;

                } else {
                    val imageView = ImageView(group.context)
                    val params = ViewPager.LayoutParams()
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
                    imageView.layoutParams = params
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    if (model is MyImageInfo) {
                        imageView.setBackgroundColor(Color.RED);
                        ImageLoader.getInstance().displayImage(model.imageUrl, imageView)
                        Log.w(TAG, "succ: model type:${model.javaClass.name} image,${model.imageUrl}");
                    } else {
                        Log.w(TAG, "ERROR: model type:${model.javaClass.name}");
                    }
                    return imageView

                }
            }

        })



        bannerCurrent.setOnItemClickListener(object : Banner.OnItemClickListener {
            override fun onClick(index: Int) {
                val iImgInfo = listImgInfo[index]
                Toast.makeText(this@MainActivity, "你点击了" + iImgInfo.bannerTitle, Toast.LENGTH_SHORT).show()


            }
        });
        val anims = ArrayList<Class<out ViewPager.PageTransformer>>()
        //        anims.add(DefaultTransformer.class);
        anims.add(AccordionTransformer::class.java)
        anims.add(BackgroundToForegroundTransformer::class.java)

        anims.add(ForegroundToBackgroundTransformer::class.java)
        //        anims.add(CubeInTransformer.class);
        anims.add(CubeOutTransformer::class.java)
        anims.add(DepthPageTransformer::class.java)
        //        anims.add(FlipHorizontalTransformer.class);
        //        anims.add(FlipVerticalTransformer.class);
        anims.add(RotateDownTransformer::class.java)
        anims.add(RotateUpTransformer::class.java)
        anims.add(ScaleInOutTransformer::class.java)
        anims.add(StackTransformer::class.java)
        //        anims.add(TabletTransformer.class);
        anims.add(ZoomInTransformer::class.java)
        anims.add(ZoomOutTranformer::class.java)
        anims.add(ZoomOutSlideTransformer::class.java)
        val aClass = anims[Random().nextInt(anims.size)]
        var transformer: ViewPager.PageTransformer?;
        try {
            transformer = aClass.newInstance()
            bannerCurrent.getInternalViewPager()?.setPageTransformer(false, transformer)//荣国动画白屏请删除此代码
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        banner.startAutoScroll()
        Log.w(TAG, aClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        banner.startAutoScroll()
        banner2.startAutoScroll()
    }


    override fun onPause() {

        super.onPause()

        banner.stopAutoScroll()
        banner2.stopAutoScroll()
    }

    open inner class MyImageInfo : Banner.IImgInfo {
        override var bannerTitle: String? = null;
        //            internal set
        var imageUrl: String? = null;
//            internal set

        override fun toString(): String {
            return "MyImageInfo [title=$bannerTitle, imgUrl=$imageUrl]"
        }

        constructor(title: String, imgUrl: String) {
            this.bannerTitle = title
            this.imageUrl = imgUrl
        }

        constructor() {}

    }

    companion object {
        private val TAG = "BannaerTest"
    }
}
