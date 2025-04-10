import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d.art3d import Poly3DCollection
import numpy as np

# Define the cuboid dimensions
cuboid_min = np.array([0, 0, 0])  # Bottom-left corner
cuboid_max = np.array([1, 1, 1])  # Top-right corner

# Define the light source position
light_source = np.array([2, 2, 2])

# Function to generate random points on the cuboid's surfaces
def generate_random_points_on_cuboid(n_points, cuboid_min, cuboid_max):
    points = []
    for _ in range(n_points):
        face = np.random.choice(['front', 'back', 'left', 'right', 'top', 'bottom'])
        if face == 'front':  # x varies, y varies, z is max
            x = np.random.uniform(cuboid_min[0], cuboid_max[0])
            y = np.random.uniform(cuboid_min[1], cuboid_max[1])
            z = cuboid_max[2]
        elif face == 'back':  # x varies, y varies, z is min
            x = np.random.uniform(cuboid_min[0], cuboid_max[0])
            y = np.random.uniform(cuboid_min[1], cuboid_max[1])
            z = cuboid_min[2]
        elif face == 'left':  # x is min, y varies, z varies
            x = cuboid_min[0]
            y = np.random.uniform(cuboid_min[1], cuboid_max[1])
            z = np.random.uniform(cuboid_min[2], cuboid_max[2])
        elif face == 'right':  # x is max, y varies, z varies
            x = cuboid_max[0]
            y = np.random.uniform(cuboid_min[1], cuboid_max[1])
            z = np.random.uniform(cuboid_min[2], cuboid_max[2])
        elif face == 'top':  # x varies, y is max, z varies
            x = np.random.uniform(cuboid_min[0], cuboid_max[0])
            y = cuboid_max[1]
            z = np.random.uniform(cuboid_min[2], cuboid_max[2])
        elif face == 'bottom':  # x varies, y is min, z varies
            x = np.random.uniform(cuboid_min[0], cuboid_max[0])
            y = cuboid_min[1]
            z = np.random.uniform(cuboid_min[2], cuboid_max[2])
        points.append([x, y, z])
    return np.array(points)

# Generate 1000 random points on the cuboid's surfaces
n_rays = 50
random_points = generate_random_points_on_cuboid(n_rays, cuboid_min, cuboid_max)

# Create a 3D plot
fig = plt.figure(figsize=(10, 8))
ax = fig.add_subplot(111, projection='3d')

# Draw the cuboid
cuboid_faces = [
    [[cuboid_min[0], cuboid_min[1], cuboid_min[2]], [cuboid_max[0], cuboid_min[1], cuboid_min[2]],
     [cuboid_max[0], cuboid_max[1], cuboid_min[2]], [cuboid_min[0], cuboid_max[1], cuboid_min[2]]],  # Bottom face
    [[cuboid_min[0], cuboid_min[1], cuboid_max[2]], [cuboid_max[0], cuboid_min[1], cuboid_max[2]],
     [cuboid_max[0], cuboid_max[1], cuboid_max[2]], [cuboid_min[0], cuboid_max[1], cuboid_max[2]]],  # Top face
    [[cuboid_min[0], cuboid_min[1], cuboid_min[2]], [cuboid_min[0], cuboid_max[1], cuboid_min[2]],
     [cuboid_min[0], cuboid_max[1], cuboid_max[2]], [cuboid_min[0], cuboid_min[1], cuboid_max[2]]],  # Left face
    [[cuboid_max[0], cuboid_min[1], cuboid_min[2]], [cuboid_max[0], cuboid_max[1], cuboid_min[2]],
     [cuboid_max[0], cuboid_max[1], cuboid_max[2]], [cuboid_max[0], cuboid_min[1], cuboid_max[2]]],  # Right face
    [[cuboid_min[0], cuboid_min[1], cuboid_min[2]], [cuboid_max[0], cuboid_min[1], cuboid_min[2]],
     [cuboid_max[0], cuboid_min[1], cuboid_max[2]], [cuboid_min[0], cuboid_min[1], cuboid_max[2]]],  # Front face
    [[cuboid_min[0], cuboid_max[1], cuboid_min[2]], [cuboid_max[0], cuboid_max[1], cuboid_min[2]],
     [cuboid_max[0], cuboid_max[1], cuboid_max[2]], [cuboid_min[0], cuboid_max[1], cuboid_max[2]]]   # Back face
]
ax.add_collection3d(Poly3DCollection(cuboid_faces, facecolors='cyan', linewidths=1, edgecolors='black', alpha=0.5))

# Draw the light source
ax.scatter(*light_source, color='yellow', s=100, label='Light Source')

# Draw rays from the light source to the random points on the cuboid
for point in random_points:
    ax.plot(
        [light_source[0], point[0]],
        [light_source[1], point[1]],
        [light_source[2], point[2]],
        color='orange',
        alpha=0.5
    )

# Set axis limits
ax.set_xlim([-1, 3])
ax.set_ylim([-1, 3])
ax.set_zlim([-1, 3])

# Add labels and legend
ax.set_xlabel('X-axis')
ax.set_ylabel('Y-axis')
ax.set_zlabel('Z-axis')
ax.legend()

# Show the plot
plt.show()