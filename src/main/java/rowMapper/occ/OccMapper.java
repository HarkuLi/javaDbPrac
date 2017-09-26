package rowMapper.occ;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.occ.OccModel;

public class OccMapper implements RowMapper<OccModel> {
	public OccModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		OccModel data = new OccModel();
		data.setId(rs.getString("id"));
		data.setName(rs.getString("name"));
		data.setState(rs.getBoolean("state"));
		return data;
	}
}
