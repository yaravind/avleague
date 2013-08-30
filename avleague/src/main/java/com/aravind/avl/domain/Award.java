package com.aravind.avl.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
@TypeAlias (value = "Award")
public class Award
{
	@GraphId
	private Long nodeId;

	@GraphProperty
	private String awardFor;

	@GraphProperty
	private Float unitPrice;

	/**
	 * The default is 1 to handle individual awards like MVP
	 */
	@GraphProperty (defaultValue = "1")
	private Short quantity = Short.valueOf((short) 1);

	public Award()
	{}

	public Award(String awardFor)
	{
		this.awardFor = StringUtil.capitalizeFirstLetter(awardFor);
	}

	public float totalCost()
	{
		return unitPrice * quantity;
	}

	public boolean isTeamAward()
	{
		return quantity > 1 ? true : false;
	}

	public Float getUnitPrice()
	{
		return unitPrice;
	}

	public void setUnitPrice(Float unitPrice)
	{
		this.unitPrice = unitPrice;
	}

	public Short getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Short quantityPrice)
	{
		this.quantity = quantityPrice;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(Long nodeId)
	{
		this.nodeId = nodeId;
	}

	public String getAwardFor()
	{
		return awardFor;
	}

	public void setAwardFor(String awardFor)
	{
		this.awardFor = awardFor;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((awardFor == null) ? 0 : awardFor.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Award other = (Award) obj;
		if (awardFor == null)
		{
			if (other.awardFor != null)
			{
				return false;
			}
		}
		else if (!awardFor.equals(other.awardFor))
		{
			return false;
		}
		if (nodeId == null)
		{
			if (other.nodeId != null)
			{
				return false;
			}
		}
		else if (!nodeId.equals(other.nodeId))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Award [nodeId=" + nodeId + ", awardFor=" + awardFor + ", unitPrice=" + unitPrice + ", quantity=" + quantity + "]";
	}
}
