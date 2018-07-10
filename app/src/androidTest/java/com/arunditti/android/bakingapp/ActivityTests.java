package com.arunditti.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.arunditti.android.bakingapp.ui.activities.DetailActivity;
import com.arunditti.android.bakingapp.ui.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by arunditti on 7/9/18.
 */

//Add annotation to specify AndroidJUnitRunner class as the default test runner
@RunWith(AndroidJUnit4.class)
public class ActivityTests {

    public static final String RECIPE_NAME = "Cheesecake";
    public static final String STEP_1 = "1";
    public static final String STEP_2 = "2";
    public static final String RECIPE_INTRODUCTION = "Recipe Introduction";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    //Clicks on a RecyclerView item and checks it opens up the DetailActivity with the correct details

    @Test
    public void clickMainActivity_RvItem_OpensDetailActivity() {

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, scrollTo()));

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.scrollToPosition(3));

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        //Checks that the DetailActivity opens with the correct recipe name displayed
        onView(withId(R.id.tv_recipe_name_detail)).check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void clickDetailActivity_RvItem_OpensRecipeStepsActivity() {

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.tv_step_description)).check(matches(withText(startsWith(STEP_1))));

        onView(withId(R.id.button_previous_step)).perform(click());

        onView(withId(R.id.tv_step_description)).check(matches(withText(RECIPE_INTRODUCTION)));

        onView(withId(R.id.exo_player_view_step_video)).check(matches(isDisplayed()));

        onView(withId(R.id.button_previous_step)).check(matches(not(isEnabled())));

        onView(withId(R.id.button_next_step)).perform(click());

        onView(withId(R.id.tv_step_description)).check(matches(withText(startsWith(STEP_1))));

        onView(withId(R.id.button_next_step)).perform(click());

        onView(withId(R.id.tv_step_description)).check(matches(withText(startsWith(STEP_2))));

    }

    @Test
    public void checkPlayerViewIsVisible_StepActivity() {

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        onView(withId(R.id.rv_recipe_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.exo_player_view_step_video)).check(matches(isDisplayed()));
    }

}
