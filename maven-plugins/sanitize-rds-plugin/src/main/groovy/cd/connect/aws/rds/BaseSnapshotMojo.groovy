package cd.connect.aws.rds

import groovy.transform.CompileStatic
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject

@CompileStatic
abstract public class BaseSnapshotMojo extends AbstractMojo {

	@Parameter(property = "rds-clone.database", required = true)
	String database
	@Parameter(property = "rds-clone.username")
	String username
	@Parameter(property = "rds-clone.password")
	String password
	@Parameter(property = "rds-clone.snapshot-wait")
	int snapshotWaitInMinutes = 20
	@Parameter(property = "rds-clone.restore-wait")
	int restoreWaitInMinutes = 20
	@Parameter(property = "rds-clone.poll-interval")
	int pollTimeInSeconds = 20
	@Parameter(property = "rds-clone.snapshotName")
	String snapshotName
	@Parameter(property = "rds-clone.aws-profile")
	String awsProfile

	@Parameter(defaultValue = '${project}', readonly = true)
	MavenProject project;

	protected RdsClone rdsClone;
	protected String realSnapshotName;

	protected void init() {
		rdsClone = new RdsClone()
		rdsClone.initialize(awsProfile)
	}
	protected String snapshot() throws MojoFailureException {
		realSnapshotName = rdsClone.snapshotDatabase(database, snapshotWaitInMinutes, pollTimeInSeconds, snapshotName)
		if (!realSnapshotName) {
			throw new MojoFailureException("Unable to create snapshot within timeframe")
		}
	}

	protected void restoreSnapshot(String dbName, String snapshotName, CreateInstanceResult createInstanceResult) {
		if (rdsClone.snapshotStatus(snapshotName, database) == null) {
			String err = "There is no Snapshot called ${snapshotName} for db ${dbName}";
			getLog().error(err)
			throw new MojoFailureException(err)
		}

		rdsClone.createDatabaseInstanceFromSnapshot(dbName, snapshotName, restoreWaitInMinutes, pollTimeInSeconds, createInstanceResult)
	}
}

