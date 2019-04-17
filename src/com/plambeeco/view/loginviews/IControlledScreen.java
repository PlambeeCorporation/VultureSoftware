package com.plambeeco.view.loginviews;

import com.plambeeco.ScreensController;

/**
 *
 * @author Neville Bulmer
 */
public interface IControlledScreen
{
    //This method will allow the injection of the Parent ScreenPane
    void setScreenParent(ScreensController screenPage);
}