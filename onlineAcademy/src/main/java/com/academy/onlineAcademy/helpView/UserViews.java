package com.academy.onlineAcademy.helpView;

import java.io.File;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.MenuItem;

public class UserViews extends VerticalLayout implements View {
	
	private static Navigator navigator = UI.getCurrent().getNavigator();;
	
	public static HorizontalLayout getTopBar(Navigator navigator) {
		HorizontalLayout layoutH = new HorizontalLayout();	
		layoutH.setSpacing(true);
		layoutH.setWidth("100%");
		layoutH.setHeight("70px");
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoResource = new FileResource(new File(basepath + "/logo.jpg"));
		Image logoImage = new Image("", logoResource);
		logoImage.setWidth("130px");
		logoImage.setHeight("60px");
		logoImage.addClickListener(e -> navigator.navigateTo(""));
		
		// MENU bar and methods to navigate to different pages	
		MenuBar profileMenu = new MenuBar();	
		MenuItem myProfileMainItem = profileMenu.addItem("My profile", VaadinIcons.MENU, null);
		myProfileMainItem.addItem("My courses", VaadinIcons.ACADEMY_CAP, createNavigationCommand("UserCourses"));
		myProfileMainItem.addItem("My orders", VaadinIcons.NEWSPAPER, createNavigationCommand("UserOrders"));
		myProfileMainItem.addItem("Settings", VaadinIcons.USER, createNavigationCommand("Settings"));
		myProfileMainItem.addItem("Log out", VaadinIcons.EXIT, logoutNavigationCommand(""));
		
		layoutH.addComponents(logoImage, profileMenu);
		layoutH.setComponentAlignment(logoImage, Alignment.TOP_LEFT);
		layoutH.setComponentAlignment(profileMenu, Alignment.BOTTOM_RIGHT);
		
		return layoutH;
	}
	
	static MenuBar.Command createNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	System.out.print("LOG");
		    	navigator.navigateTo(navigationView);
		    }
		};
	}
	
	static MenuBar.Command logoutNavigationCommand(String navigationView) {
		return new MenuBar.Command() {
		    public void menuSelected(MenuItem selectedItem) {
		    	UI ui = UI.getCurrent();
				VaadinSession session = ui.getSession();
				session.setAttribute("user-id", null);
		    	navigator.navigateTo(navigationView);
		    }
		};
	}

}
