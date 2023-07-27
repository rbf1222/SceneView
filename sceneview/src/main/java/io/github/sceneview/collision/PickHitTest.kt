package io.github.sceneview.collision

import android.view.MotionEvent
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.PickHitResult
import com.google.ar.sceneform.collision.Collider
import com.google.ar.sceneform.collision.Ray
import io.github.sceneview.SceneView
import io.github.sceneview.node.Node
import java.util.*

/**
 * Tests to see if a motion event is touching any nodes within the scene, based on a ray hit test
 * whose origin is the screen position of the motion event, and outputs a PickHitResult containing
 * the node closest to the screen.
 *
 * @param motionEvent         the motion event to use for the test
 * @param focusableOnly Filter the PickHitResult on only selectable nodes
 * @return the result includes the first node that was hit by the motion event (may be null), and
 * information about where the motion event hit the node in world-space
 */
fun SceneView.pickHitTest(motionEvent: MotionEvent, focusableOnly: Boolean): PickHitResult =
    pickHitTest(camera.motionEventToRay(motionEvent), focusableOnly)

/**
 * Tests to see if a ray is hitting any nodes within the scene and outputs a PickHitResult
 * containing the node closest to the ray origin that intersects with the ray.
 *
 * @param ray                 the ray to use for the test
 * @param focusableOnly Filter the PickHitResult on only selectable nodes
 * @return the result includes the first node that was hit by the ray (may be null), and
 * information about where the ray hit the node in world-space
 * @see Camera.screenPointToRay
 */
fun SceneView.pickHitTest(ray: Ray, focusableOnly: Boolean) = PickHitResult()
    .apply {
        collisionSystem.raycast(ray, this, focusableOnly)?.let {
            node = it.transformProvider as? Node
        }
    }

/**
 * Tests to see if a motion event is touching any nodes within the scene and returns a list of
 * HitTestResults containing all of the nodes that were hit, sorted by distance.
 *
 * @param motionEvent The motion event to use for the test.
 * @return Populated with a PickHitResult for each node that was hit sorted by distance. Empty if
 * no nodes were hit.
 */
fun SceneView.pickHitTests(motionEvent: MotionEvent): ArrayList<PickHitResult> =
    pickHitTests(camera.motionEventToRay(motionEvent))

/**
 * Tests to see if a ray is hitting any nodes within the scene and returns a list of
 * HitTestResults containing all of the nodes that were hit, sorted by distance.
 *
 * @param ray The ray to use for the test.
 * @return Populated with a PickHitResult for each node that was hit sorted by distance. Empty if
 * no nodes were hit.
 * @see Camera.screenPointToRay
 */
fun SceneView.pickHitTests(ray: Ray) = ArrayList<PickHitResult>().apply {
    collisionSystem.raycastAll(
        ray,
        this,
        { resultPick: PickHitResult, collider: Collider ->
            resultPick.node = collider.transformProvider as Node
        }
    ) { PickHitResult() }
}
